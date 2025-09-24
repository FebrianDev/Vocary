package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

@Dao
interface SyncDataDao {

    // Vocabulary
    @Query("SELECT * FROM vocabulary WHERE isSync = 0")
    suspend fun getUnsyncedVocabularies(): List<VocabularyEntity>
    @Update suspend fun updateVocabulary(vocabulary: VocabularyEntity)
    @Update suspend fun updateVocabularies(vocabularies: List<VocabularyEntity>)

    // Daily Progress
    @Query("SELECT * FROM daily_progress WHERE isSync = 0")
    suspend fun getUnsyncedDailyProgress(): List<DailyProgressEntity>
    @Update suspend fun updateDailyProgress(entity: DailyProgressEntity)

    // Game Session
    @Query("SELECT * FROM game_session WHERE isSync = 0")
    suspend fun getUnsyncedGameSessions(): List<GameSessionEntity>
    @Update suspend fun updateGameSession(entity: GameSessionEntity)

    // Generated Vocab
    @Query("SELECT * FROM generate_vocab WHERE isSync = 0")
    suspend fun getUnsyncedGeneratedVocab(): List<GeneratedVocabEntity>
    @Update suspend fun updateGeneratedVocab(entity: GeneratedVocabEntity)

    // Search Vocabulary
    @Query("SELECT * FROM search_vocabulary WHERE isSync = 0")
    suspend fun getUnsyncedSearchVocabulary(): List<SearchVocabularyEntity>
    @Update suspend fun updateSearchVocabulary(entity: SearchVocabularyEntity)

    // Streak
    @Query("SELECT * FROM streak WHERE isSync = 0")
    suspend fun getUnsyncedStreaks(): List<StreakEntity>
    @Update suspend fun updateStreak(entity: StreakEntity)

    // User
    @Query("SELECT * FROM user WHERE isSync = 0")
    suspend fun getUnsyncedUsers(): List<UserEntity>
    @Update suspend fun updateUser(entity: UserEntity)
}