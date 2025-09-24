package com.febriandev.vocary.domain

import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

data class Vocabulary(
    val id: String = "",
    val word: String = "",
    val phonetic: String = "",
    val audio: String = "",
    val sourceUrl: String = "",
    val definition: String = "",
    val partOfSpeech: String = "",
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val examples: List<String> = emptyList(),
    val definitions: List<UnifiedDefinitionEntity> = emptyList(),

    val note: String = "",

    // --- SRS fields ---
    val srsDueDate: Long = System.currentTimeMillis(), // kapan kata ini akan muncul lagi
    val srsStatus: SrsStatus = SrsStatus.NEW,

    val isOwnWord: Boolean = false,
    val ownWordTimestamp: Long? = null,

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
        phonetic = this.pronunciation ?: firstPhonetic ?: "",
        audio = firstAudio ?: "",
        sourceUrl = sourceUrls.firstOrNull() ?: firstUrl ?: "",
        definition = firstDefinition?.definition ?: "",
        partOfSpeech = firstDefinition?.partOfSpeech ?: "",
        synonyms = firstDefinition?.synonyms ?: emptyList(),
        examples = firstDefinition?.examples ?: emptyList(),
        definitions = definitions,

        note = note,

        srsDueDate = srsDueDate,
        srsStatus = srsStatus,

        isOwnWord = isOwnWord,
        ownWordTimestamp = ownWordTimestamp ?: 0L,

        isReport = isReport,

        isFavorite = this.isFavorite,
        isHistory = this.isHistory,
        favoriteTimestamp = this.favoriteTimestamp,
        historyTimestamp = this.historyTimestamp
    )
}

