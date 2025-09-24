package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Dao
interface DownloadDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyProgress(items: List<DailyProgressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGameSessions(items: List<GameSessionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGeneratedVocabs(items: List<GeneratedVocabEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSearchVocabularies(items: List<SearchVocabularyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStreaks(items: List<StreakEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateVocabularies(items: List<VocabularyEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(item: UserEntity)

}