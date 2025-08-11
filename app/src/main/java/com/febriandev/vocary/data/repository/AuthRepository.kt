package com.febriandev.vocary.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> =
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser> =
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> =
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun signOut() {
        auth.signOut()
    }
}
