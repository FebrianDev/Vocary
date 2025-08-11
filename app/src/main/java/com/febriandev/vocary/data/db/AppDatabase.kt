package com.febriandev.vocary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.febriandev.vocary.data.db.dao.GenerateVocabDao
import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Database(
    entities = [GeneratedVocabEntity::class, VocabularyEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun generateVocabDao(): GenerateVocabDao
    abstract fun vocabularyDao(): VocabularyDao

}