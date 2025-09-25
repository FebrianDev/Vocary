package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.domain.toVocabulary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VocabularyRepository @Inject constructor(private val vocabularyDao: VocabularyDao) {
    suspend fun getAllVocabulary(currentTime: Long): List<Vocabulary> {
        return vocabularyDao.getAllVocabulary(currentTime).map { it.toVocabulary() }.shuffled()
    }

    // suspend fun getFavorites(): List<VocabularyEntity> = vocabularyDao.getFavorites()

    suspend fun updateSrs(id: String, srsStatus: SrsStatus, dueDate: Long) {
        vocabularyDao.updateSrs(id, srsStatus, dueDate)
    }

    suspend fun updateNote(
        id: String,
        note: String
    ) = vocabularyDao.updateNote(id, note)

    suspend fun addReport(
        id: String
    ) = vocabularyDao.addReport(id)

    suspend fun getCountNewOrNope(): Int {
        return vocabularyDao.getCountNewOrNope()
    }

    suspend fun isFavorite(id: String): Boolean = vocabularyDao.isFavorite(id)

    suspend fun addToFavorite(id: String) {
        vocabularyDao.addToFavorite(id, System.currentTimeMillis())
    }

    suspend fun removeFromFavorite(id: String) {
        vocabularyDao.removeFromFavorite(id)
    }

    fun getHistory(): Flow<List<Vocabulary>> =
        vocabularyDao.getHistory().map { it.map { it.toVocabulary() } }

    fun searchHistory(query: String): Flow<List<Vocabulary>> =
        vocabularyDao.searchHistory(query).map { it.map { it.toVocabulary() } }

    suspend fun addToHistory(id: String) {
        vocabularyDao.addToHistory(id, System.currentTimeMillis())
    }

    suspend fun getAllOwnWord(): List<Vocabulary> {
        return vocabularyDao.getAllOwnWord().map { it.toVocabulary() }
    }

    suspend fun searchOwnWord(query: String): List<Vocabulary> {
        return vocabularyDao.searchOwnWord(query).map { it.toVocabulary() }
    }

    suspend fun insertVocabulary(vocabulary: VocabularyEntity) {
        vocabularyDao.insertVocabulary(vocabulary)
    }

    suspend fun getVocabularyByWord(word: String): VocabularyEntity? {
        return vocabularyDao.getVocabularyByWord(word)
    }

    suspend fun deleteAllData() {
        vocabularyDao.deleteVocabulary()
        vocabularyDao.deleteGame()
        vocabularyDao.deleteUser()
        vocabularyDao.deleteGenerate()
        vocabularyDao.deleteSearch()
        vocabularyDao.deleteStreak()
        vocabularyDao.deleteProgress()
    }

}