package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.GameSessionEntity

data class GameSessionDto(
    val sessionId: String = "",
    val startTime: Long = 0,
    val endTime: Long = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val score: Int = 0,
    val isSync: Boolean = false,
) {
    fun toEntity(): GameSessionEntity = GameSessionEntity(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        totalQuestions = totalQuestions,
        correctAnswers = correctAnswers,
        wrongAnswers = wrongAnswers,
        score = score,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: GameSessionEntity) = GameSessionDto(
            sessionId = entity.sessionId,
            startTime = entity.startTime,
            endTime = entity.endTime,
            totalQuestions = entity.totalQuestions,
            correctAnswers = entity.correctAnswers,
            wrongAnswers = entity.wrongAnswers,
            score = entity.score,
            isSync = entity.isSync
        )
    }
}
