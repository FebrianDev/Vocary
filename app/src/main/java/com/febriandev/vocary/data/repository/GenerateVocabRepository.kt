package com.febriandev.vocary.data.repository

import android.util.Log
import com.febriandev.vocary.BuildConfig
import com.febriandev.vocary.data.api.DictionaryApiService
import com.febriandev.vocary.data.api.OpenAiApiService
import com.febriandev.vocary.data.api.WordsApiService
import com.febriandev.vocary.data.db.dao.GenerateVocabDao
import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.data.db.entity.convertToVocabListFromArray
import com.febriandev.vocary.data.db.entity.toEntity
import com.febriandev.vocary.data.request.OpenAIMessage
import com.febriandev.vocary.data.request.OpenAIRequest
import com.febriandev.vocary.data.response.DictionaryResponse
import com.febriandev.vocary.data.response.WordsApiResponse
import com.febriandev.vocary.utils.generateRandomId
import javax.inject.Inject

class GenerateVocabRepository @Inject constructor(
    private val openAiApi: OpenAiApiService,
    private val dictionaryApi: DictionaryApiService,
    private val wordsApiService: WordsApiService,
    private val generateDao: GenerateVocabDao,
    private val vocabularyDao: VocabularyDao
) {

    suspend fun generateVocabulary(topic: String, level: String, userId: String) {
        val prompt = """
         You are an English teacher AI.

        Generate **exactly 20 unique** English vocabulary words related to **"$topic"**, 
        suitable for **$level** level learners (userId = $userId).
        
        ⚠️ Rules:
        - Return only a **pure JSON array** of **lowercase words** (no explanations, no numbering).
        - Avoid duplicates from earlier generations for this user.
        - Prioritize variety: include a mix of **common, intermediate, and less common words**.
        - Cover different parts of speech (nouns, verbs, adjectives, expressions) when possible.
        - If the topic is broad, do not stick to only the most obvious words.
        - If common words are likely already used, choose fresher, rarer alternatives.
            
            ✅ Example output:
            ["apple", "banana", "grape", "orange", "melon"]
            """.trimIndent()

        val response = openAiApi.getChatCompletion(
            OpenAIRequest(
                messages = listOf(OpenAIMessage("user", prompt))
            ),
            BuildConfig.HEADER_TOKEN
        )

        val json = response.choices.firstOrNull()?.message?.content?.trim() ?: return
        val vocabList = convertToVocabListFromArray(json)

        generateDao.insertVocabList(vocabList)
    }

    suspend fun processVocabulary() {
        generateDao.getPendingVocab().forEach { vocab ->
            try {

                val existing = vocabularyDao.getVocabularyByWord(vocab.word)
                if (existing != null) {
                    // sudah ada → tandai sebagai processed, skip API call
                    generateDao.updateVocabDetail(vocab.word)
                    return@forEach
                }

                val dictionaryResponse = dictionaryApi.getWordDefinition(vocab.word)
                val wordsResponse = wordsApiService.getWordInfo(
                    word = vocab.word,
                    apiKey = BuildConfig.API_WORDS_API
                )

                val vocabulary = combineResponses(wordsResponse, dictionaryResponse)

                if (vocabulary != null) vocabularyDao.insertVocabulary(vocabulary)
                generateDao.updateVocabDetail(vocab.word)
            } catch (e: Exception) {
                generateDao.updateVocabDetail(vocab.word)
                Log.e("insertDetail", "Error inserting detail for word ${vocab.word}", e)
            }
        }
    }

    suspend fun getWord(word: String): VocabularyEntity? {
        return try {
            val dictionaryResponse = dictionaryApi.getWordDefinition(word)
            val wordsResponse = wordsApiService.getWordInfo(
                word = word,
                apiKey = BuildConfig.API_WORDS_API
            )

            val vocabulary = combineResponses(wordsResponse, dictionaryResponse)
            vocabulary
        } catch (e: Exception) {
            Log.e("insertDetail", "Error inserting detail for word $word", e)
            null
        }
    }

    fun combineResponses(
        wordsApi: WordsApiResponse?,
        dictionaryApi: List<DictionaryResponse>?
    ): VocabularyEntity? {

        // Tambahan dari Dictionary API (jika ada)
        val phonetics = dictionaryApi?.flatMap { it.phonetics ?: emptyList() } ?: emptyList()
        val sourceUrls = dictionaryApi?.flatMap { it.sourceUrls ?: emptyList() } ?: emptyList()

        // ✅ 1. Kalau WordsAPI ada → langsung pakai WordsAPI + tambahkan phonetics & sourceUrls dari Dictionary
        if (wordsApi != null) {
            val wordsApiDefinitions = wordsApi.results?.mapNotNull { result ->
                result.definition?.let { definition ->
                    UnifiedDefinitionEntity(
                        definition = definition,
                        partOfSpeech = result.partOfSpeech,
                        synonyms = result.synonyms ?: emptyList(),
                        examples = result.examples ?: emptyList(),
                    )
                }
            } ?: emptyList()

            return VocabularyEntity(
                id = generateRandomId(),
                word = wordsApi.word,
                pronunciation = if (wordsApi.pronunciation == null) "" else wordsApi.pronunciation.all,
                phonetics = phonetics.map { it.toEntity() },
                definitions = wordsApiDefinitions,
                sourceUrls = sourceUrls
            )
        }

        // ✅ 2. Kalau WordsAPI null → fallback pakai Dictionary API full
        val dictionaryDefinitions = dictionaryApi?.flatMap { item ->
            item.meanings?.flatMap { meaning ->
                meaning.definitions?.mapNotNull { def ->
                    def.definition?.let { definition ->
                        UnifiedDefinitionEntity(
                            definition = definition,
                            partOfSpeech = meaning.partOfSpeech,
                            synonyms = def.synonyms ?: emptyList(),
                            examples = if (def.example == null) emptyList() else listOf(def.example)
                        )
                    }
                } ?: emptyList()
            } ?: emptyList()
        } ?: emptyList()

        return dictionaryApi?.firstOrNull()?.word?.let { word ->
            VocabularyEntity(
                id = generateRandomId(),
                word = word,
                phonetics = phonetics.map { it.toEntity() },
                definitions = dictionaryDefinitions,
                sourceUrls = sourceUrls
            )
        }
    }

}