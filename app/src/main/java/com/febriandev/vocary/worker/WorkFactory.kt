package com.febriandev.vocary.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.febriandev.vocary.data.repository.DownloadDataRepository
import com.febriandev.vocary.data.repository.GenerateVocabRepository
import com.febriandev.vocary.data.repository.SyncDataRepository
import javax.inject.Inject

class WorkFactory @Inject constructor(
    private val generateVocabRepository: GenerateVocabRepository,
    private val syncDataRepository: SyncDataRepository,
    private val downloadDataRepository: DownloadDataRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            GenerateVocabWorker::class.java.name ->
                GenerateVocabWorker(appContext, workerParameters, generateVocabRepository)

            ProcessVocabWorker::class.java.name ->
                ProcessVocabWorker(appContext, workerParameters, generateVocabRepository)

            SyncDataWorker::class.java.name ->
                SyncDataWorker(appContext, workerParameters, syncDataRepository)

            DownloadDataWorker::class.java.name ->
                DownloadDataWorker(appContext, workerParameters, downloadDataRepository)

            else -> null
        }
    }
}