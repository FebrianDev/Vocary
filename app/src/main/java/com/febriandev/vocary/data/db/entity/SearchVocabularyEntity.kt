package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.febriandev.vocary.data.db.converters.VocabularyConverters

@Entity(tableName = "search_vocabulary")
@TypeConverters(VocabularyConverters::class)
data class SearchVocabularyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val word: String,
    val phonetics: List<PhoneticEntity> = emptyList(),
    val definitions: List<UnifiedDefinitionEntity> = emptyList(),
    val sourceUrls: List<String> = emptyList(),
    val isSync: Boolean = false,
)

fun SearchVocabularyEntity.toVocabularyEntity(): VocabularyEntity {
    return VocabularyEntity(
        id = id,
        word = word,
        pronunciation = phonetics.firstOrNull()?.text,
        phonetics = phonetics,
        definitions = definitions,
        sourceUrls = sourceUrls,
        note = "",
        isFavorite = false,
        isHistory = false,
        favoriteTimestamp = null,
        historyTimestamp = null,
        isReport = false,
        isOwnWord = true,
        ownWordTimestamp = System.currentTimeMillis(),
        srsDueDate = System.currentTimeMillis(),
        srsStatus = SrsStatus.NEW,
        isSync = isSync
    )
}
