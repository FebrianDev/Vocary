package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "streak")
data class StreakEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val streakType: String, // "APP_OPEN" atau "DAILY_GOAL"
    val date: String,       // format: yyyy-MM-dd (misalnya "2025-08-16")
    val isCompleted: Boolean
)