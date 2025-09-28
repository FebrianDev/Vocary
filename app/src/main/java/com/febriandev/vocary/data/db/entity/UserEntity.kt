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
    val xp:Int? = 0,
    val targetVocabulary: Int,
    val learningGoal: String?,
    val vocabLevel: String?,
    val vocabTopic: String?,
    val premium: Boolean,
    val premiumDuration: String?,
    val isRevenueCat: Boolean = false,
    val deviceName: String?,
    val deviceId: String?,
    val isSync: Boolean = false,
) : Parcelable
