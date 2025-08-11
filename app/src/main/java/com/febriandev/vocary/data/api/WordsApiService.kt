package com.febriandev.vocary.data.api

import com.febriandev.vocary.data.response.WordsApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WordsApiService {

    @GET("words/{word}")
    suspend fun getWordInfo(
        @Path("word") word: String,
        @Header("x-rapidapi-host") host: String = "wordsapiv1.p.rapidapi.com",
        @Header("x-rapidapi-key") apiKey: String
    ): WordsApiResponse
}