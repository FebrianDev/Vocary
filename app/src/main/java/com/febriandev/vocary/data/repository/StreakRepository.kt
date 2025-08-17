package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.StreakDao
import com.febriandev.vocary.data.db.entity.StreakEntity
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

    suspend fun getStreaksByType(streakType: String): List<StreakEntity> =
        withContext(Dispatchers.IO) {
            streakDao.getStreaksByType(streakType)
        }

    suspend fun getStreakByDate(streakType: String, date: String): StreakEntity? =
        withContext(Dispatchers.IO) {
            streakDao.getStreakByDate(streakType, date)
        }

    suspend fun clearStreakByType(streakType: String) = withContext(Dispatchers.IO) {
        streakDao.clearStreakByType(streakType)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        streakDao.clearAll()
    }
}