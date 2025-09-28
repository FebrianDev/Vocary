package com.febriandev.vocary.data.db.dao

import androidx.room.*
import com.febriandev.vocary.data.db.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE user SET premium = :premium, premiumDuration = :premiumDuration WHERE id = :id")
    suspend fun updatePremiumFields(id: String, premium: Boolean, premiumDuration: String?)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clearUsers()
}
