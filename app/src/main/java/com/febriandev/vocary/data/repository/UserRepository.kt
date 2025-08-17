package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.UserDao
import com.febriandev.vocary.data.db.entity.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

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

    suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    suspend fun clearUsers() {
        userDao.clearUsers()
    }
}
