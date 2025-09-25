package com.febriandev.vocary.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
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
    val premium: Boolean,
    val premiumDuration: String?,
    val deviceName: String?,
    val deviceId: String?,
    val isSync: Boolean = false,
) : Parcelable
