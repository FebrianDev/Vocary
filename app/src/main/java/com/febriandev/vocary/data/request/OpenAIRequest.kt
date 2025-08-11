package com.febriandev.vocary.data.request

data class OpenAIRequest(
    val messages: List<OpenAIMessage>,
    val max_tokens: Int = 150,
    val temperature: Double = 0.5,
    val top_p: Double = 1.0,
    val model: String = "gpt-35-turbo-2"
)

data class OpenAIMessage(
    val role: String = "user",
    val content: String
)

