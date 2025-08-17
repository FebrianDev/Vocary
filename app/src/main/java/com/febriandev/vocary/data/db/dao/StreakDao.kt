package com.febriandev.vocary.data.db.dao

import androidx.room.*
import com.febriandev.vocary.data.db.entity.StreakEntity

@Dao
interface StreakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreak(streak: StreakEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreaks(streaks: List<StreakEntity>)

    @Query("SELECT * FROM streak WHERE streakType = :streakType ORDER BY date ASC")
    suspend fun getStreaksByType(streakType: String): List<StreakEntity>

    @Query("SELECT * FROM streak WHERE streakType = :streakType AND date = :date LIMIT 1")
    suspend fun getStreakByDate(streakType: String, date: String): StreakEntity?

    @Query("DELETE FROM streak WHERE streakType = :streakType")
    suspend fun clearStreakByType(streakType: String)

    @Query("DELETE FROM streak")
    suspend fun clearAll()
}
