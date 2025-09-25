package com.febriandev.vocary.data.model

import com.febriandev.vocary.data.db.entity.UserEntity

data class UserDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val age: Int? = null,
    val targetVocabulary: Int = 0,
    val learningGoal: String? = null,
    val vocabLevel: String? = null,
    val vocabTopic: String? = null,
    val premium: Boolean = false,
    val premiumDuration: String? = null,
    val deviceName: String? = null,
    val deviceId: String? = null,
    val isSync: Boolean = false,
) {
    fun toEntity(): UserEntity = UserEntity(
        id = id,
        name = name,
        email = email,
        photoUrl = photoUrl,
        age = age,
        targetVocabulary = targetVocabulary,
        learningGoal = learningGoal,
        vocabLevel = vocabLevel,
        vocabTopic = vocabTopic,
        premium = premium,
        premiumDuration = premiumDuration,
        deviceName = deviceName,
        deviceId = deviceId,
        isSync = isSync
    )

    companion object {
        fun fromEntity(entity: UserEntity) = UserDto(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            photoUrl = entity.photoUrl,
            age = entity.age,
            targetVocabulary = entity.targetVocabulary,
            learningGoal = entity.learningGoal,
            vocabLevel = entity.vocabLevel,
            vocabTopic = entity.vocabTopic,
            premium = entity.premium,
            premiumDuration = entity.premiumDuration,
            deviceName = entity.deviceName,
            deviceId = entity.deviceId,
            isSync = entity.isSync
        )
    }
}
