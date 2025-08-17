package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "game_session")
data class GameSessionEntity(
    @PrimaryKey val sessionId: String = UUID.randomUUID().toString(),
    val startTime: Long,
    val endTime: Long,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val score: Int
)