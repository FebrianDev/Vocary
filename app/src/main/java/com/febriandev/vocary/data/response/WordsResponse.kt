package com.febriandev.vocary.data.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class WordsApiResponse(
    val word: String,
    val results: List<WordsApiResult>?,
    val pronunciation: PronunciationResponse? = null
)

data class WordsApiResult(
    val definition: String?,
    val partOfSpeech: String?,
    val synonyms: List<String>?,
    val typeOf: List<String>?,
    val hasTypes: List<String>?,
    val derivation: List<String>?,
    val also: List<String>?,
    val examples: List<String>?
)


data class PronunciationResponse(
    val all: String? = null,
)

class PronunciationDeserializer : JsonDeserializer<PronunciationResponse?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PronunciationResponse? {
        if (json == null || json.isJsonNull) return null

        return if (json.isJsonPrimitive) {
            PronunciationResponse(all = json.asString)
        } else if (json.isJsonObject) {
            // langsung ambil field "all"
            val obj = json.asJsonObject
            PronunciationResponse(all = obj.get("all")?.asString)
        } else {
            null
        }
    }
}
