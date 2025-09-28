package com.febriandev.vocary.data.repository

import android.util.Log
import com.febriandev.vocary.data.db.dao.DownloadDataDao
import com.febriandev.vocary.data.model.DailyProgressDto
import com.febriandev.vocary.data.model.GameSessionDto
import com.febriandev.vocary.data.model.GeneratedVocabDto
import com.febriandev.vocary.data.model.SearchVocabularyDto
import com.febriandev.vocary.data.model.StreakDto
import com.febriandev.vocary.data.model.UserDto
import com.febriandev.vocary.data.model.VocabularyDto
import com.febriandev.vocary.data.model.toEntity
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

            dao

            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("vocabularies")
                .get()
                .await()

            val vocabList = snapshot.documents.mapNotNull { doc ->
                Log.d("DownloadRepo", "Raw doc: ${doc.data}") // c
                val data = doc.toObject(VocabularyDto::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )

                data?.toEntity()
            }
            dao.deleteVocabularyIsSync()
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
                val data = doc.toObject(DailyProgressDto::class.java)?.copy(
                    date = doc.id,
                    isSync = true
                )

                data?.toEntity()
            }
            dao.deleteDailyProgressIsSync()
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
                val data = doc.toObject(GameSessionDto::class.java)?.copy(
                    sessionId = doc.id,
                    isSync = true
                )

                data?.toEntity()
            }
            dao.deleteGameSessionsIsSync()
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
                .collection("generated_vocab")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                val data = doc.toObject(GeneratedVocabDto::class.java)?.copy(
                    id = doc.id.toInt(),
                    isSync = true
                )

                data?.toEntity()
            }
            dao.deleteGeneratedVocabsIsSync()
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
                val data = doc.toObject(SearchVocabularyDto::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )

                data?.toEntity()
            }
            dao.deleteSearchVocabulariesIsSync()
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
                .collection("streaks")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                val data = doc.toObject(StreakDto::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )

                data?.toEntity()
            }

            dao.deleteStreaksIsSync()
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
                .collection("users")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserDto::class.java)?.copy(
                    id = doc.id,
                    isSync = true
                )?.toEntity()
            }

            if (list.isNotEmpty()) {
                dao.deleteUserIsSync()
                dao.insertOrUpdateUser(list.first()) // ambil yang pertama, kalau cuma 1 user
            } else {
                Log.w("DownloadRepo", "No user found in sub-collection for $userId")
            }
        } catch (e: Exception) {
            Log.e("DownloadRepo", "Failed to fetch user", e)
        }
    }
}
