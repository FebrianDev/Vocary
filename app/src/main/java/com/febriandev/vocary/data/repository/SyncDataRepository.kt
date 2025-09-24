package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.SyncDataDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class SyncDataRepository @Inject constructor(
    private val syncDataDao: SyncDataDao,
    private val firestore: FirebaseFirestore
) {

    suspend fun syncUnsyncedVocabularies(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("vocabularies")
        val unsynced = syncDataDao.getUnsyncedVocabularies()
        unsynced.forEach { vocab ->
            try {
                collection.document(vocab.id).set(vocab).await()
                syncDataDao.updateVocabulary(vocab.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedDailyProgress(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("daily_progress")
        val unsynced = syncDataDao.getUnsyncedDailyProgress()
        unsynced.forEach { item ->
            try {
                collection.document(item.date).set(item).await()
                syncDataDao.updateDailyProgress(item.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedGameSessions(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("game_sessions")
        val unsynced = syncDataDao.getUnsyncedGameSessions()
        unsynced.forEach { session ->
            try {
                collection.document(session.sessionId).set(session).await()
                syncDataDao.updateGameSession(session.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedGeneratedVocab(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("generated_vocab")
        val unsynced = syncDataDao.getUnsyncedGeneratedVocab()
        unsynced.forEach { gv ->
            try {
                collection.document(gv.id.toString()).set(gv).await()
                syncDataDao.updateGeneratedVocab(gv.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedSearchVocabulary(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("search_vocabulary")
        val unsynced = syncDataDao.getUnsyncedSearchVocabulary()
        unsynced.forEach { search ->
            try {
                collection.document(search.id).set(search).await()
                syncDataDao.updateSearchVocabulary(search.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedStreaks(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("streaks")
        val unsynced = syncDataDao.getUnsyncedStreaks()
        unsynced.forEach { streak ->
            try {
                collection.document(streak.id).set(streak).await()
                syncDataDao.updateStreak(streak.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncUnsyncedUsers(userId: String) {
        val collection = firestore.collection("users")
            .document(userId).collection("users")
        val unsynced = syncDataDao.getUnsyncedUsers()
        unsynced.forEach { user ->
            try {
                collection.document(user.id).set(user).await()
                syncDataDao.updateUser(user.copy(isSync = true))
            } catch (_: Exception) {
            }
        }
    }

    suspend fun syncAll(userId: String) {
        syncUnsyncedVocabularies(userId)
        syncUnsyncedDailyProgress(userId)
        syncUnsyncedGameSessions(userId)
        syncUnsyncedGeneratedVocab(userId)
        syncUnsyncedSearchVocabulary(userId)
        syncUnsyncedStreaks(userId)
        syncUnsyncedUsers(userId)
    }

}