package com.febriandev.vocary.data.repository

import com.febriandev.vocary.BuildConfig
import com.febriandev.vocary.data.api.DictionaryApiService
import com.febriandev.vocary.data.api.WordsApiService
import com.febriandev.vocary.data.db.dao.SearchVocabularyDao
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.toEntity
import com.febriandev.vocary.data.response.DictionaryResponse
import com.febriandev.vocary.data.response.WordsApiResponse
import com.febriandev.vocary.utils.generateRandomId
import javax.inject.Inject

class SearchVocabularyRepository @Inject constructor(
    private val dao: SearchVocabularyDao,
    private val dictionaryApi: DictionaryApiService,
    private val wordsApiService: WordsApiService,
) {

    suspend fun searchVocabulary(query: String): List<SearchVocabularyEntity> {
        // 1. Cari di lokal DB dulu
        val localResults = dao.searchWords(query)
        if (localResults.isNotEmpty()) return localResults

        val results = mutableListOf<SearchVocabularyEntity>()

        // 2. Fetch kata utama (gabungan WordsApi + DictionaryApi)
        val mainResult = fetchFromApis(query)
        if (mainResult == null)
            return emptyList()

        results.add(mainResult)

        // 3. Ambil related words (derivatives + also)
        val relatedWords = mainResult.definitions
            .flatMap { def -> def.derivatives + def.also }
            .distinct().filter { it.isNotEmpty() }
            .take(5)

        // 4. Fetch untuk related words
        for (related in relatedWords) {
            val relatedResult = fetchFromApis(related)
            if (relatedResult != null) results.add(relatedResult)
        }

        // 5. Simpan ke DB
        dao.insertWords(results)

        return results
    }

    private suspend fun fetchFromApis(word: String): SearchVocabularyEntity? {
        return try {
            val dictionaryResponse = dictionaryApi.getWordDefinition(word)
            val wordsResponse = wordsApiService.getWordInfo(
                word = word,
                apiKey = BuildConfig.API_WORDS_API
            )

            val phonetics = dictionaryResponse.flatMap { it.phonetics ?: emptyList() }
            val sourceUrls = dictionaryResponse.flatMap { it.sourceUrls ?: emptyList() }

            SearchVocabularyEntity(
                id = generateRandomId(),
                word = word,
                phonetics = phonetics.map { it.toEntity() },
                definitions = mergeDefinitions(wordsResponse, dictionaryResponse),
                sourceUrls = sourceUrls,

            )
        } catch (e: retrofit2.HttpException) {
            null
        } catch (e: Exception) {

            null
        }
    }

    private fun mergeDefinitions(
        wordsApi: WordsApiResponse?,
        dictionaryApi: List<DictionaryResponse>?
    ): List<UnifiedDefinitionEntity> {
        val list = mutableListOf<UnifiedDefinitionEntity>()

        // 🔹 Prioritas WordsApi
        val wordApiResults = wordsApi?.results
        if (!wordApiResults.isNullOrEmpty()) {
            wordApiResults.forEach {
                list.add(
                    UnifiedDefinitionEntity(
                        definition = it.definition ?: "",
                        partOfSpeech = it.partOfSpeech,
                        examples = it.examples ?: emptyList(),
                        synonyms = it.synonyms ?: emptyList(),
                        derivatives = it.derivation ?: emptyList(),
                        also = it.also ?: emptyList()
                    )
                )
            }
            return list // ✅ langsung return, tidak lanjut ke dictionaryApi
        }

        // 🔹 Fallback DictionaryApi kalau WordsApi kosong/null
        dictionaryApi?.forEach { resp ->
            resp.meanings?.forEach { meaning ->
                meaning.definitions?.forEach { def ->
                    list.add(
                        UnifiedDefinitionEntity(
                            definition = def.definition ?: "",
                            partOfSpeech = meaning.partOfSpeech,
                            examples = if (def.example == null) emptyList() else listOf(def.example),
                            synonyms = def.synonyms ?: emptyList(),
                            derivatives = emptyList(),
                            also = emptyList()
                        )
                    )
                }
            }
        }

        return list
    }

}
