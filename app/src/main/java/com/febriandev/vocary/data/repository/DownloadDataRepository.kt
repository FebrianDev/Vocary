package com.febriandev.vocary.data.repository

import android.util.Log
import com.febriandev.vocary.data.db.dao.DownloadDataDao
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.GeneratedVocabEntity
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadDataRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: DownloadDataDao
) {

    // ---------------- Vocabulary ----------------
    suspend fun getAndSaveVocabularies(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("vocabularies")
                .get()
                .await()

            val vocabList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(VocabularyEntity::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )
            }
            dao.insertOrUpdateVocabularies(vocabList)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch vocabularies", e)
        }
    }

    // ---------------- Daily Progress ----------------
    suspend fun getAndSaveDailyProgress(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("daily_progress")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(DailyProgressEntity::class.java)?.copy(
                    date = doc.id,
                    isSync = true
                )
            }
            dao.insertOrUpdateDailyProgress(list)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch daily progress", e)
        }
    }

    // ---------------- Game Session ----------------
    suspend fun getAndSaveGameSessions(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("game_session")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(GameSessionEntity::class.java)?.copy(
                    sessionId = doc.id,
                    isSync = true
                )
            }
            dao.insertOrUpdateGameSessions(list)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch game sessions", e)
        }
    }

    // ---------------- Generated Vocab ----------------
    suspend fun getAndSaveGeneratedVocabs(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("generate_vocab")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(GeneratedVocabEntity::class.java)?.copy(
                    id = doc.id.toInt(),
                    isSync = true
                )
            }
            dao.insertOrUpdateGeneratedVocabs(list)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch generated vocabs", e)
        }
    }

    // ---------------- Search Vocabulary ----------------
    suspend fun getAndSaveSearchVocabularies(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("search_vocabulary")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(SearchVocabularyEntity::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )
            }
            dao.insertOrUpdateSearchVocabularies(list)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch search vocabularies", e)
        }
    }

    // ---------------- Streak ----------------
    suspend fun getAndSaveStreaks(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("streak")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(StreakEntity::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )
            }
            dao.insertOrUpdateStreaks(list)
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch streaks", e)
        }
    }

    // ---------------- User ----------------
    suspend fun getAndSaveUser(userId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            snapshot.toObject(UserEntity::class.java)?.copy(
                id = snapshot.id,
                isSync = true
            )?.let { user ->
                dao.insertOrUpdateUser(user)
            }
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch user", e)
        }
    }
}
