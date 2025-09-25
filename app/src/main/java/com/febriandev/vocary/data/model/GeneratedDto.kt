package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity

data class GeneratedVocabDto(
    val id:Int = 0,
    val word: String = "",
    val isStatus: Boolean = false,
    val isSync: Boolean = false,
) {
    fun toEntity(): GeneratedVocabEntity = GeneratedVocabEntity(
        id = id,
        word = word,
        isStatus = isStatus,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: GeneratedVocabEntity) = GeneratedVocabDto(
            id = entity.id,
            word = entity.word,
            isStatus = entity.isStatus,
            isSync = entity.isSync
        )
    }
}
