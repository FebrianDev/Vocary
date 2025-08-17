package com.febriandev.vocary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String, // misalnya pakai UID Firebase
    val name: String,
    val email: String,
    val photoUrl: String?,
    val age: Int?,
    val targetVocabulary: Int, // target harian
    val learningGoal: String?, // tujuan belajar misalnya "TOEFL", "Daily Conversation"
    val vocabLevel: String?, // Beginner, Intermediate, Advanced
    val vocabTopic: String?, // misalnya "Business", "Travel"
    val isPremium: Boolean,
    val premiumDuration: Long?, // timestamp expire premium
    val deviceName: String?,
    val deviceId: String?
)
