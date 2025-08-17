package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febriandev.vocary.data.db.entity.GameSessionEntity

@Dao
interface GameSessionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(session: GameSessionEntity)

    @Query("SELECT * FROM game_session ORDER BY endTime DESC")
    suspend fun getAll(): List<GameSessionEntity>

    @Query("SELECT * FROM game_session WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getById(sessionId: String): GameSessionEntity?
}
