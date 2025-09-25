package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.DailyProgressEntity

data class DailyProgressDto(
    val date: String = "",
    val progress: Int = 0,
    val isGoalAchieved: Boolean = false,
    val listVocabulary: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSync: Boolean = false,
) {
    fun toEntity(): DailyProgressEntity = DailyProgressEntity(
        date = date,
        progress = progress,
        isGoalAchieved = isGoalAchieved,
        listVocabulary = listVocabulary,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: DailyProgressEntity) = DailyProgressDto(
            date = entity.date,
            progress = entity.progress,
            isGoalAchieved = entity.isGoalAchieved,
            listVocabulary = entity.listVocabulary,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isSync = entity.isSync
        )
    }
}
