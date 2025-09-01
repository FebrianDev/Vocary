package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.febriandev.vocary.data.db.converters.VocabularyConverters
import com.febriandev.vocary.data.response.PhoneticResponse

@Entity(tableName = "vocabulary")
@TypeConverters(VocabularyConverters::class)
data class VocabularyEntity(
    @PrimaryKey
    val id: String,
    val word: String,
    val pronunciation: String? = null,
    val phonetics: List<PhoneticEntity> = emptyList(),
    val definitions: List<UnifiedDefinitionEntity> = emptyList(),
    val sourceUrls: List<String> = emptyList(),

    val note: String = "",

    val isFavorite: Boolean = false,
    val isHistory: Boolean = false,
    val favoriteTimestamp: Long? = null,
    val historyTimestamp: Long? = null,

    val isReport: Boolean = false,

    val isOwnWord: Boolean = false,
    val ownWordTimestamp: Long? = null,

    // --- SRS fields ---
    val srsDueDate: Long = System.currentTimeMillis(), // kapan kata ini akan muncul lagi
    val srsStatus: SrsStatus = SrsStatus.NEW,

    val isSync: Boolean = false,
)

enum class SrsStatus {
    NEW,
    NOPE,
    MAYBE,
    KNOWN
}

data class PhoneticEntity(
    val text: String?,
    val audio: String?,
    val sourceUrl: String? = null // optional
)

data class UnifiedDefinitionEntity(
    val definition: String,
    val partOfSpeech: String?,
    val synonyms: List<String> = emptyList(),
    val examples: List<String> = emptyList(),
    val derivatives: List<String> = emptyList(),
    val also: List<String> = emptyList()
)

fun PhoneticResponse.toEntity(): PhoneticEntity {
    return PhoneticEntity(
        text = this.text,
        audio = this.audio,
        sourceUrl = this.sourceUrl
    )
}


