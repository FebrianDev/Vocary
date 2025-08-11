package com.febriandev.vocary.data.api

import com.febriandev.vocary.data.response.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {

    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(@Path("word") word: String): List<DictionaryResponse>
}