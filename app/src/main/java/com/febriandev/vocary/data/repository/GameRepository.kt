package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.GameSessionDao
import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val vocabularyDao: VocabularyDao,
    private val sessionDao: GameSessionDao
) {
    suspend fun getAllVocabulary(): List<VocabularyEntity> {
        return vocabularyDao.getAllVocabulary(System.currentTimeMillis())
            .filter { it.definitions.isNotEmpty() }
    }

    suspend fun insertSession(session: GameSessionEntity) {
        sessionDao.insert(session)
    }

    suspend fun getAllSessions(): List<GameSessionEntity> {
        return sessionDao.getAll()
    }
}
