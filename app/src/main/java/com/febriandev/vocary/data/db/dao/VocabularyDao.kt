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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(vocabulary: List<VocabularyEntity>)

    @Query(
        """
    SELECT DISTINCT * 
    FROM vocabulary
    ORDER BY srsDueDate ASC, id DESC
"""
    )
    suspend fun getAllVocabulary(): List<VocabularyEntity>

    @Query(
        """
        UPDATE vocabulary 
        SET srsStatus = :status, 
            srsDueDate = :dueDate 
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

    @Query("UPDATE vocabulary SET isFavorite = 1, favoriteTimestamp = :timestamp WHERE id = :id")
    suspend fun addToFavorite(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE vocabulary SET isFavorite = 0, favoriteTimestamp = NULL WHERE id = :id")
    suspend fun removeFromFavorite(id: String)

    // --- History ---
    @Query("SELECT * FROM vocabulary WHERE isHistory = 1 ORDER BY historyTimestamp DESC")
    fun getHistory(): Flow<List<VocabularyEntity>>

    @Query(" SELECT * FROM vocabulary WHERE word LIKE '%' || :query || '%' ORDER BY historyTimestamp DESC")
    fun searchHistory(query: String): Flow<List<VocabularyEntity>>

    @Query("UPDATE vocabulary SET isHistory = 1, historyTimestamp = :timestamp WHERE id = :id")
    suspend fun addToHistory(id: String, timestamp: Long = System.currentTimeMillis())
}