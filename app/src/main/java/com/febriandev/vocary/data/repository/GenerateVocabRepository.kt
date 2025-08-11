package com.febriandev.vocary.data.repository

import android.util.Log
import com.febrian.vocery.utils.generateRandomId
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
import javax.inject.Inject

class GenerateVocabRepository @Inject constructor(
    private val openAiApi: OpenAiApiService,
    private val dictionaryApi: DictionaryApiService,
    private val wordsApiService: WordsApiService,
    private val generateDao: GenerateVocabDao,
    private val vocabularyDao: VocabularyDao
) {

    suspend fun generateVocabulary(topic: String, level: String) {
        val prompt = """
           You are an English teacher AI.

            Generate **exactly 20 unique** English vocabulary words related to **"$topic"**, suitable for **$level** level learners.

            ⚠️ Return only a **pure JSON array** of **lowercase words**, no explanations, no numbering.
            
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
                val dictionaryResponse = dictionaryApi.getWordDefinition(vocab.word)
                val wordsResponse = wordsApiService.getWordInfo(
                    word = vocab.word,
                    apiKey = BuildConfig.API_WORDS_API
                )

                val vocabulary = combineResponses(wordsResponse, dictionaryResponse)

                if (vocabulary != null) vocabularyDao.insertVocabulary(vocabulary)
                generateDao.updateVocabDetail(vocab.word)

//                if (response.isNotEmpty()) {
//                    val data = response.map {
//                        it.toVocabDetailEntity(false)
//                    }
//                    vocabDetailDao.insertDetail(data)
//                    dao.updateVocabDetail(vocab.word)
//                } else {
//                    dao.updateVocabDetail(vocab.word)
//                }
            } catch (e: Exception) {
                generateDao.updateVocabDetail(vocab.word)
                Log.e("insertDetail", "Error inserting detail for word ${vocab.word}", e)
            }
        }
    }

    fun combineResponses(
        wordsApi: WordsApiResponse?,
        dictionaryApi: List<DictionaryResponse>?
    ): VocabularyEntity? {
        if (wordsApi == null && (dictionaryApi == null || dictionaryApi.isEmpty())) return null

        val word = wordsApi?.word ?: dictionaryApi?.firstOrNull()?.word ?: return null

        // Step 1: Ambil definisi dari Words API
        val wordsApiDefinitions = wordsApi?.results?.mapNotNull { result ->
            result.definition?.let { definition ->
                UnifiedDefinitionEntity(
                    definition = definition,
                    partOfSpeech = result.partOfSpeech,
                    synonyms = result.synonyms ?: emptyList(),
                    examples = result.examples ?: emptyList(),
                )
            }
        } ?: emptyList()

        // Step 2: Ambil definisi dari Dictionary API (hanya yang belum ada di Words API)
        val dictionaryDefinitions = dictionaryApi?.flatMap { item ->
            item.meanings?.flatMap { meaning ->
                meaning.definitions?.mapNotNull { def ->
                    def.definition?.takeIf { newDef ->
                        // Hindari duplikat definisi yang sudah ada di Words API
                        wordsApiDefinitions.none { it.definition.equals(newDef, ignoreCase = true) }
                    }?.let { definition ->
                        UnifiedDefinitionEntity(
                            definition = definition,
                            partOfSpeech = meaning.partOfSpeech,
                            synonyms = def.synonyms ?: emptyList(),
                            examples = emptyList(), // Dictionary API tidak punya examples
                        )
                    }
                } ?: emptyList()
            } ?: emptyList()
        } ?: emptyList()

        // Step 3: Gabungkan semua definisi
        val combinedDefinitions = wordsApiDefinitions + dictionaryDefinitions

        // Step 4: Ambil phonetics dan sourceUrls dari Dictionary API
        val phonetics = dictionaryApi?.flatMap { it.phonetics ?: emptyList() } ?: emptyList()
        val sourceUrls = dictionaryApi?.flatMap { it.sourceUrls ?: emptyList() } ?: emptyList()

        return VocabularyEntity(
            id = generateRandomId(),
            word = word,
            phonetics = phonetics.map { it.toEntity() },
            definitions = combinedDefinitions,
            sourceUrls = sourceUrls
        )
    }

}