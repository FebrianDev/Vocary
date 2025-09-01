package com.febriandev.vocary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.febriandev.vocary.data.db.dao.StreakDao
import com.febriandev.vocary.data.db.entity.StreakDay
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.mapToStreakDay
import com.febriandev.vocary.data.db.entity.mapToStreakDays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreakRepository @Inject constructor(private val streakDao: StreakDao) {

    suspend fun insertStreak(streak: StreakEntity) = withContext(Dispatchers.IO) {
        streakDao.insertStreak(streak)
    }

    suspend fun insertStreaks(streaks: List<StreakEntity>) = withContext(Dispatchers.IO) {
        streakDao.insertStreaks(streaks)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getStreaksByType(streakType: String): List<StreakDay> =
        withContext(Dispatchers.IO) {
            mapToStreakDays(streakDao.getStreaksByType(streakType))
        }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getStreakByDate(streakType: String, date: String): StreakDay? =
        withContext(Dispatchers.IO) {
            val streak = streakDao.getStreakByDate(streakType, date)
            if (streak == null) null
            else mapToStreakDay(streak)
        }

    suspend fun clearStreakByType(streakType: String) = withContext(Dispatchers.IO) {
        streakDao.clearStreakByType(streakType)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        streakDao.clearAll()
    }
}