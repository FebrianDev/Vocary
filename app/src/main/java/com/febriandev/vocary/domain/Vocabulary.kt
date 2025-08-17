package com.febriandev.vocary.domain

import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

data class Vocabulary(
    val id: String = "",
    val word: String = "",
    val phonetic: String = "",              // dari phonetics[0].text
    val audio: String = "",                 // dari phonetics[0].audio
    val sourceUrl: String = "",             // dari phonetics[0].sourceUrl
    val definition: String = "",            // dari definitions[0].definition
    val partOfSpeech: String = "",          // dari definitions[0].partOfSpeech
    val synonyms: List<String> = emptyList(),
    val examples: List<String> = emptyList(),
    val definitions: List<UnifiedDefinitionEntity> = emptyList(),

    val note: String = "",

    // --- SRS fields ---
    val srsDueDate: Long = System.currentTimeMillis(), // kapan kata ini akan muncul lagi
    val srsStatus: SrsStatus = SrsStatus.NEW,

    val isReport: Boolean = false,

    val isFavorite: Boolean = false,
    val isHistory: Boolean = false,
    val favoriteTimestamp: Long? = null,
    val historyTimestamp: Long? = null
)

fun VocabularyEntity.toVocabulary(): Vocabulary {
    val firstPhonetic = phonetics.firstOrNull { !it.text.isNullOrBlank() }?.text
    val firstAudio = phonetics.firstOrNull { !it.audio.isNullOrBlank() }?.audio
    val firstUrl = phonetics.firstOrNull { !it.sourceUrl.isNullOrBlank() }?.sourceUrl
    val firstDefinition = definitions.firstOrNull()

    return Vocabulary(
        id = this.id,
        word = this.word,
        phonetic = firstPhonetic ?: "",
        audio = firstAudio ?: "",
        sourceUrl = firstUrl ?: "",
        definition = firstDefinition?.definition ?: "",
        partOfSpeech = firstDefinition?.partOfSpeech ?: "",
        synonyms = firstDefinition?.synonyms ?: emptyList(),
        examples = firstDefinition?.examples ?: emptyList(),
        definitions = definitions,

        note = note,

        srsDueDate = srsDueDate,
        srsStatus = srsStatus,

        isReport = isReport,

        isFavorite = this.isFavorite,
        isHistory = this.isHistory,
        favoriteTimestamp = this.favoriteTimestamp,
        historyTimestamp = this.historyTimestamp
    )
}

