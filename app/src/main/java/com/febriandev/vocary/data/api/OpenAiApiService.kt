package com.febriandev.vocary.data.api

import com.febriandev.vocary.data.request.OpenAIRequest
import com.febriandev.vocary.data.response.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiService {
    @POST("openai/deployments/gpt-35-turbo-2/chat/completions?api-version=2025-01-01-preview")
    suspend fun getChatCompletion(
        @Body request: OpenAIRequest,
        @Header("Authorization") credentials: String
    ): OpenAIResponse
}