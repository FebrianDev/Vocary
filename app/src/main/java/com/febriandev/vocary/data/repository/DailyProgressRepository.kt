package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.DailyProgressDao
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import javax.inject.Inject

class DailyProgressRepository @Inject constructor(
    private val dailyProgressDao: DailyProgressDao,
) {

    suspend fun getProgress(date: String): DailyProgressEntity? {
        return dailyProgressDao.getProgress(date)
    }

    suspend fun insert(progress: DailyProgressEntity) {
        dailyProgressDao.insert(progress)
    }

    suspend fun update(progress: DailyProgressEntity) {
        dailyProgressDao.update(progress)
    }
}