package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.StreakEntity

data class StreakDto(
    val id: String = "",
    val streakType: String = "",
    val date: String = "",
    val isCompleted: Boolean = false,
    val isSync: Boolean = false,
) {
    fun toEntity(): StreakEntity = StreakEntity(
        id = id,
        streakType = streakType,
        date = date,
        isCompleted = isCompleted,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: StreakEntity) = StreakDto(
            id = entity.id,
            streakType = entity.streakType,
            date = entity.date,
            isCompleted = entity.isCompleted,
            isSync = entity.isSync
        )
    }
}
