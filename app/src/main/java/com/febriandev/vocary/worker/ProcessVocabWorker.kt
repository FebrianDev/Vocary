package com.febriandev.vocary.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.febriandev.vocary.data.repository.GenerateVocabRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ProcessVocabWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val repository: GenerateVocabRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.processVocabulary()
            Result.success()
        } catch (e: Exception) {
            Log.e("ProcessVocabWorker", "Error: ${e.message}", e)
            Result.failure()
        }
    }
}