package com.febriandev.vocary.domain

data class AppUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val isPremium: Boolean = false
)
