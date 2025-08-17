package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.febriandev.vocary.data.db.entity.DailyProgressEntity

@Dao
interface DailyProgressDao {
    @Query("SELECT * FROM daily_progress WHERE date = :date LIMIT 1")
    suspend fun getProgress(date: String): DailyProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: DailyProgressEntity)

    @Update
    suspend fun update(progress: DailyProgressEntity)
}