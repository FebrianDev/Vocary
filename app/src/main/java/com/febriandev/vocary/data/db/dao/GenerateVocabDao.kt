package com.febriandev.vocary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity

@Dao
interface GenerateVocabDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabList(vocabList: List<GeneratedVocabEntity>)

    @Query("SELECT * FROM generate_vocab WHERE isStatus = 0")
    suspend fun getPendingVocab(): List<GeneratedVocabEntity>

    @Query("UPDATE generate_vocab SET isStatus = 1 WHERE word=:word")
    suspend fun updateVocabDetail(word:String)

    @Query("DELETE FROM generate_vocab")
    suspend fun deleteVocab()
}
