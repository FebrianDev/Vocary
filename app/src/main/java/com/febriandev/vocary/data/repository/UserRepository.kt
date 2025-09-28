package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.UserDao
import com.febriandev.vocary.data.db.entity.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) {

    fun insertUser(user: UserEntity, onResult: (Boolean, String?) -> Unit) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    // Get user
    fun getUser(userId: String, onResult: (UserEntity?) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = doc.toObject(UserEntity::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    suspend fun insertOrUpdateUser(user: UserEntity) {
        userDao.insertOrUpdateUser(user)
    }

    suspend fun getUserById(id: String): UserEntity? {
        return userDao.getUserById(id)
    }

    suspend fun getCurrentUser(): UserEntity? {
        return userDao.getCurrentUser()
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun updatePremiumFields(id: String, premium: Boolean, premiumDuration: String?){
        userDao.updatePremiumFields(id, premium, premiumDuration)
    }

    suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    suspend fun clearUsers() {
        userDao.clearUsers()
    }
}
