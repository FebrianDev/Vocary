package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity

@Dao
interface SearchVocabularyDao {

    // Exact match (untuk cek apakah kata sudah ada di DB)
    @Query("SELECT * FROM search_vocabulary WHERE word = :word LIMIT 1")
    suspend fun getWordExact(word: String): SearchVocabularyEntity?

    // Search pakai LIKE (misalnya untuk fitur search box)
    @Query("SELECT * FROM search_vocabulary WHERE word LIKE '%' || :query || '%'")
    suspend fun searchWords(query: String): List<SearchVocabularyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: SearchVocabularyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<SearchVocabularyEntity>)
}
