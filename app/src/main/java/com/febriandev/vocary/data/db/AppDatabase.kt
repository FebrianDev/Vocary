package com.febriandev.vocary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.febriandev.vocary.data.db.dao.DailyProgressDao
import com.febriandev.vocary.data.db.dao.DownloadDataDao
import com.febriandev.vocary.data.db.dao.GameSessionDao
import com.febriandev.vocary.data.db.dao.GenerateVocabDao
import com.febriandev.vocary.data.db.dao.SearchVocabularyDao
import com.febriandev.vocary.data.db.dao.StreakDao
import com.febriandev.vocary.data.db.dao.SyncDataDao
import com.febriandev.vocary.data.db.dao.UserDao
import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Database(
    entities = [GeneratedVocabEntity::class, VocabularyEntity::class, GameSessionEntity::class, DailyProgressEntity::class, StreakEntity::class, UserEntity::class, SearchVocabularyEntity::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun generateVocabDao(): GenerateVocabDao
    abstract fun vocabularyDao(): VocabularyDao

    abstract fun gameSessionDao(): GameSessionDao
    abstract fun dailyProgressDao(): DailyProgressDao
    abstract fun streakDao(): StreakDao
    abstract fun userDao(): UserDao

    abstract fun searchVocabularyDao(): SearchVocabularyDao
    abstract fun syncDataDao(): SyncDataDao

    abstract fun downloadDataDao(): DownloadDataDao

}