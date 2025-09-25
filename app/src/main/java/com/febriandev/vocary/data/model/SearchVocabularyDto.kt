package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.PhoneticEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity

data class SearchVocabularyDto(
    val id: String = "",
    val word: String = "",
    val phonetics: List<PhoneticEntity> = emptyList(),
    val definitions: List<UnifiedDefinitionEntity> = emptyList(),
    val sourceUrls: List<String> = emptyList(),
    val isSync: Boolean = false,
) {
    fun toEntity(): SearchVocabularyEntity = SearchVocabularyEntity(
        id = id,
        word = word,
        phonetics = phonetics,
        definitions = definitions,
        sourceUrls = sourceUrls,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: SearchVocabularyEntity) = SearchVocabularyDto(
            id = entity.id,
            word = entity.word,
            phonetics = entity.phonetics,
            definitions = entity.definitions,
            sourceUrls = entity.sourceUrls,
            isSync = entity.isSync
        )
    }
}
