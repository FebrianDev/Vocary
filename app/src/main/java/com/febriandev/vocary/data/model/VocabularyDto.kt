package com.febriandev.vocary.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import androidx.annotation.Keep
import com.febriandev.vocary.data.db.entity.PhoneticEntity
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Keep
@IgnoreExtraProperties
data class VocabularyDto(
    var id: String = "",
    var word: String = "",
    var pronunciation: String? = null,
    var phonetics: List<PhoneticDto> = emptyList(),
    var definitions: List<UnifiedDefinitionDto> = emptyList(),
    var sourceUrls: List<String> = emptyList(),

    var note: String = "",

    var isFavorite: Boolean = false,
    var isHistory: Boolean = false,
    var favoriteTimestamp: Long? = null,
    var historyTimestamp: Long? = null,

    var isReport: Boolean = false,

    var isOwnWord: Boolean = false,
    var ownWordTimestamp: Long? = null,

    var srsDueDate: Long = 0L,
    var srsStatus: String = SrsStatus.NEW.name,

    var isSync: Boolean = false,
)

data class PhoneticDto(
    var text: String? = null,
    var audio: String? = null,
    var sourceUrl: String? = null
)

data class UnifiedDefinitionDto(
    var definition: String = "",
    var partOfSpeech: String? = null,
    var synonyms: List<String> = emptyList(),
    var examples: List<String> = emptyList(),
    var derivatives: List<String> = emptyList(),
    var also: List<String> = emptyList()
)

fun VocabularyDto.toEntity(): VocabularyEntity = VocabularyEntity(
    id = id,
    word = word,
    pronunciation = pronunciation,
    phonetics = phonetics.map { PhoneticEntity(it.text, it.audio, it.sourceUrl) },
    definitions = definitions.map {
        UnifiedDefinitionEntity(
            definition = it.definition,
            partOfSpeech = it.partOfSpeech,
            synonyms = it.synonyms,
            examples = it.examples,
            derivatives = it.derivatives,
            also = it.also
        )
    },
    sourceUrls = sourceUrls,
    note = note,
    isFavorite = isFavorite,
    isHistory = isHistory,
    favoriteTimestamp = favoriteTimestamp,
    historyTimestamp = historyTimestamp,
    isReport = isReport,
    isOwnWord = isOwnWord,
    ownWordTimestamp = ownWordTimestamp,
    srsDueDate = srsDueDate,
    srsStatus = SrsStatus.valueOf(srsStatus),
    isSync = isSync
)

fun VocabularyEntity.toDto(): VocabularyDto = VocabularyDto(
    id = id,
    word = word,
    pronunciation = pronunciation,
    phonetics = phonetics.map { PhoneticDto(it.text, it.audio, it.sourceUrl) },
    definitions = definitions.map {
        UnifiedDefinitionDto(
            definition = it.definition,
            partOfSpeech = it.partOfSpeech,
            synonyms = it.synonyms,
            examples = it.examples,
            derivatives = it.derivatives,
            also = it.also
        )
    },
    sourceUrls = sourceUrls,
    note = note,
    isFavorite = isFavorite,
    isHistory = isHistory,
    favoriteTimestamp = favoriteTimestamp,
    historyTimestamp = historyTimestamp,
    isReport = isReport,
    isOwnWord = isOwnWord,
    ownWordTimestamp = ownWordTimestamp,
    srsDueDate = srsDueDate,
    srsStatus = srsStatus.name,
    isSync = isSync
)
