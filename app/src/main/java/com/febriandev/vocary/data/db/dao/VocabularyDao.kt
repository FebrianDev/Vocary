package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(vocabulary: VocabularyEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVocabulary(vocabulary: List<VocabularyEntity>)

    @Query("SELECT * FROM vocabulary WHERE word = :word LIMIT 1")
    suspend fun getVocabularyByWord(word: String): VocabularyEntity?

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary 
    WHERE
      isOwnWord = 0 

    ORDER BY id DESC 
    LIMIT 1
"""
    )
    fun getVocab(): VocabularyEntity?

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary 
    WHERE isReport = 0
      AND (srsStatus = 'NEW' OR srsDueDate <= :currentTime)
    ORDER BY 
      CASE WHEN srsStatus = 'NEW' THEN 0 ELSE 1 END,
      srsDueDate ASC,
      id DESC
    """
    )
    suspend fun getAllVocabulary(currentTime: Long): List<VocabularyEntity>

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary 
    WHERE isReport = 0
      AND (srsStatus = 'NEW' OR srsDueDate <= :currentTime)
    ORDER BY 
      CASE WHEN id = :preferredId THEN 0 ELSE 1 END,
      CASE WHEN srsStatus = 'NEW' THEN 0 ELSE 1 END,
      srsDueDate ASC,
      id DESC
    """
    )
    suspend fun getAllVocabulary(
        currentTime: Long,
        preferredId: String? = null
    ): List<VocabularyEntity>


    @Query(
        """
        UPDATE vocabulary 
        SET srsStatus = :status, 
            srsDueDate = :dueDate, isSync=0
        WHERE id = :id
    """
    )
    suspend fun updateSrs(
        id: String,
        status: SrsStatus,
        dueDate: Long
    )

    @Query(
        """
        UPDATE vocabulary 
        SET note = :note, isSync=0
        WHERE id = :id
    """
    )
    suspend fun updateNote(
        id: String,
        note: String
    )

    @Query(
        """
        UPDATE vocabulary 
        SET isReport=1, isSync=0
        WHERE id = :id
    """
    )
    suspend fun addReport(
        id: String
    )

    @Query(
        """
    SELECT COUNT(*) 
    FROM vocabulary
    WHERE srsStatus = :newStatus OR srsStatus = :nopeStatus
"""
    )
    suspend fun getCountNewOrNope(
        newStatus: SrsStatus = SrsStatus.NEW,
        nopeStatus: SrsStatus = SrsStatus.NOPE
    ): Int


    // --- Favorites ---
    @Query("SELECT * FROM vocabulary WHERE isFavorite = 1 ORDER BY favoriteTimestamp DESC")
    fun getFavorites(): Flow<List<VocabularyEntity>>

    @Query(" SELECT * FROM vocabulary WHERE word LIKE '%' || :query || '%' ORDER BY favoriteTimestamp DESC")
    fun searchFavorite(query: String): Flow<List<VocabularyEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM vocabulary WHERE id = :id AND isFavorite = 1)")
    suspend fun isFavorite(id: String): Boolean

    @Query("UPDATE vocabulary SET isFavorite = 1, isSync=0, favoriteTimestamp = :timestamp, isSync=0 WHERE id = :id")
    suspend fun addToFavorite(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE vocabulary SET isFavorite = 0, favoriteTimestamp = NULL, isSync=0 WHERE id = :id")
    suspend fun removeFromFavorite(id: String)

    // --- History ---
    @Query("SELECT * FROM vocabulary WHERE isHistory = 1 ORDER BY historyTimestamp DESC")
    fun getHistory(): Flow<List<VocabularyEntity>>

    @Query(" SELECT * FROM vocabulary WHERE word LIKE '%' || :query || '%' ORDER BY historyTimestamp DESC")
    fun searchHistory(query: String): Flow<List<VocabularyEntity>>

    @Query("UPDATE vocabulary SET isHistory = 1, historyTimestamp = :timestamp, isSync=0 WHERE id = :id")
    suspend fun addToHistory(id: String, timestamp: Long = System.currentTimeMillis())

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary 
    WHERE isOwnWord = 1
    ORDER BY id DESC
"""
    )
    suspend fun getAllOwnWord(): List<VocabularyEntity>

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary
    WHERE word LIKE '%' || :query || '%' 
      AND isOwnWord = 1 

    ORDER BY id DESC
"""
    )
    suspend fun searchOwnWord(query: String): List<VocabularyEntity>

    @Query("DELETE FROM vocabulary")
    suspend fun deleteVocabulary()

    @Query("DELETE FROM daily_progress")
    suspend fun deleteProgress()

    @Query("DELETE FROM generate_vocab")
    suspend fun deleteGenerate()

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Query("DELETE FROM game_session")
    suspend fun deleteGame()

    @Query("DELETE FROM streak")
    suspend fun deleteStreak()

    @Query("DELETE FROM search_vocabulary")
    suspend fun deleteSearch()

}