package com.febriandev.vocary.data.response

data class DictionaryResponse(
    val word: String,
    val phonetic: String?,
    val phonetics: List<PhoneticResponse>?,
    val meanings: List<MeaningResponse>?,
    val license: LicenseResponse?,
    val sourceUrls: List<String>?
)

data class PhoneticResponse(
    val text: String?,
    val audio: String?,
    val sourceUrl: String?,
    val license: LicenseResponse? = null
)

data class MeaningResponse(
    val partOfSpeech: String?,
    val definitions: List<DefinitionResponse>?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)

data class DefinitionResponse(
    val definition: String?,
    val example: String? = null,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)

data class LicenseResponse(
    val name: String?,
    val url: String?
)
