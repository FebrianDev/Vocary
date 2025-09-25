package com.febriandev.vocary.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.febriandev.vocary.data.repository.DownloadDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DownloadDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: DownloadDataRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            val userId = params.inputData.getString("uid")
            if (userId != null) {
                repository.getAndSaveUser(userId)
                repository.getAndSaveVocabularies(userId)
                repository.getAndSaveDailyProgress(userId)
                repository.getAndSaveGameSessions(userId)
                repository.getAndSaveGeneratedVocabs(userId)
                repository.getAndSaveSearchVocabularies(userId)
                repository.getAndSaveStreaks(userId)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("DownloadAllWorker", "Failed", e)
            Result.retry()
        }
    }
}