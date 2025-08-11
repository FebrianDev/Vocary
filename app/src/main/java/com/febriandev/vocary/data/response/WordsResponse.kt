package com.febriandev.vocary.data.response

data class WordsApiResponse(
    val word: String,
    val results: List<WordsApiResult>?
)

data class WordsApiResult(
    val definition: String?,
    val partOfSpeech: String?,
    val synonyms: List<String>?,
    val typeOf: List<String>?,
    val hasTypes: List<String>?,
    val derivation: List<String>?,
    val examples: List<String>?
)
