package com.febriandev.vocary.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val isPremium: Boolean = false
) : Parcelable
