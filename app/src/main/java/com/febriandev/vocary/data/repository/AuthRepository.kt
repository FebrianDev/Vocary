package com.febriandev.vocary.data.repository

import android.content.Context
import android.util.Log
import com.febriandev.vocary.data.model.UserDto
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    //  private val auth = FirebaseAuth.getInstance()

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

    suspend fun getExistUser(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("users")
                .get()
                .await()
            Log.d("DownloadRepo", "Sub-collection docs: ${snapshot.documents}")
            !snapshot.isEmpty
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch user", e)
            false
        }
    }


    suspend fun signOut(gso: GoogleSignInOptions, context: Context) {
        auth.signOut()

        GoogleSignIn.getClient(context, gso).signOut().await()
    }
}
