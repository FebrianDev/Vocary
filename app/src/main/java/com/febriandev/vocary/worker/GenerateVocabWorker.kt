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
class GenerateVocabWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    @Assisted private val repository: GenerateVocabRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("ProcessVocabWorker", "StartGenerate")
        val topic = inputData.getString("topic") ?: return Result.failure()
        val level = inputData.getString("level") ?: return Result.failure()
        val userId = inputData.getString("userId") ?: return Result.failure()

        Log.d("ProcessVocabWorker", "Start: topic=$topic, level=$level")

        return try {
            repository.generateVocabulary(topic, level, userId)
            Log.d("ProcessVocabWorker", "SUCCESS")
            Result.success()
        } catch (e: Exception) {
            Log.d("ProcessVocabWorker", "Error: ${e.message}", e)
            Result.failure()
        }
    }
}
