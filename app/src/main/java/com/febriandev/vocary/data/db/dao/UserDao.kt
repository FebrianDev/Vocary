package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("UPDATE user SET premium = :premium, premiumDuration = :premiumDuration, isSync=0 WHERE id = :id")
    suspend fun updatePremiumFields(id: String, premium: Boolean, premiumDuration: String?)

    @Query("UPDATE user SET xp = :xp, isSync=0 WHERE id = :id")
    suspend fun updateXp(id: String, xp: Int)


    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clearUsers()
}
