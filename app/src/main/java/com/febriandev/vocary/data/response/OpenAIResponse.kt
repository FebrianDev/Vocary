package com.febriandev.vocary.data.response

import com.febriandev.vocary.data.request.OpenAIMessage

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: OpenAIMessage
)
