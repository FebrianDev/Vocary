package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Dao
interface DownloadDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateDailyProgress(items: List<DailyProgressEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateGameSessions(items: List<GameSessionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateGeneratedVocabs(items: List<GeneratedVocabEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateSearchVocabularies(items: List<SearchVocabularyEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateStreaks(items: List<StreakEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateVocabularies(items: List<VocabularyEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateUser(item: UserEntity)

    @Query("DELETE FROM daily_progress WHERE isSync = 1")
    suspend fun deleteDailyProgressIsSync()

    @Query("DELETE FROM game_session WHERE isSync = 1")
    suspend fun deleteGameSessionsIsSync()

    @Query("DELETE FROM generate_vocab WHERE isSync = 1")
    suspend fun deleteGeneratedVocabsIsSync()

    @Query("DELETE FROM search_vocabulary WHERE isSync = 1")
    suspend fun deleteSearchVocabulariesIsSync()

    @Query("DELETE FROM streak WHERE isSync = 1")
    suspend fun deleteStreaksIsSync()

    @Query("DELETE FROM vocabulary WHERE isSync = 1")
    suspend fun deleteVocabularyIsSync()

    @Query("DELETE FROM user WHERE isSync = 1")
    suspend fun deleteUserIsSync()
}