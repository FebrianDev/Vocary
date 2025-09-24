package com.febriandev.vocary

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.febriandev.vocary.utils.ConnHelper
import com.febriandev.vocary.utils.showMessage
import com.febriandev.vocary.worker.DownloadDataWorker
import com.febriandev.vocary.worker.GenerateVocabWorker
import com.febriandev.vocary.worker.ProcessVocabWorker
import com.febriandev.vocary.worker.SyncDataWorker

abstract class BaseActivity : ComponentActivity() {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(applicationContext)

    fun isWorkerRunning(uniqueName: String): Boolean {
        val workInfoList = workManager.getWorkInfosForUniqueWork(uniqueName).get()
        return workInfoList.any {
            it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
        }
    }

    fun workerStateLiveData(uniqueName: String): LiveData<WorkInfo.State?> {
        return workManager.getWorkInfosForUniqueWorkLiveData(uniqueName)
            .map { workInfos -> workInfos.lastOrNull()?.state }
    }


    fun startGenerateProcess(topic: String, level: String) {

        if (!isWorkerRunning("GenerateVocabulary") && ConnHelper.hasConnection(applicationContext)) {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val generateRequest = OneTimeWorkRequest.Builder(GenerateVocabWorker::class.java)
                .setConstraints(constraints)
                .setInputData(
                    workDataOf(
                        "topic" to topic,
                        "level" to level,
                    )
                )
                .build()

            val processRequest =
                OneTimeWorkRequestBuilder<ProcessVocabWorker>().addTag("ProcessVocab").build()
//
//        val translateRequest = OneTimeWorkRequestBuilder<TranslateWorker>()
//            .addTag("Translate")
//            .build()
//
//        val vocabTranslateRequest = OneTimeWorkRequestBuilder<VocabTranslateWorker>()
//            .addTag("VocabTranslate")
//            .build()

            // Chain: generate -> process
            workManager.beginUniqueWork(
                "GenerateVocabulary", // Gunakan nama unik
                ExistingWorkPolicy.KEEP, // Jangan jalankan lagi jika masih aktif
                generateRequest
            ).then(processRequest)
                //  .then(translateRequest)
                //  .then(vocabTranslateRequest)
                .enqueue()
        }
    }

    fun syncData() {
        if (!isWorkerRunning("SYNC_DATA")) {
            showMessage("Sync Data...")
            val workRequest = OneTimeWorkRequestBuilder<SyncDataWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .build()

            workManager.enqueueUniqueWork(
                "SYNC_DATA",
                ExistingWorkPolicy.KEEP,
                workRequest
            )
        } else {
            showMessage("Synchronizing...")
        }
    }

    fun downloadData(uid: String) {
      //  if (!isWorkerRunning("DOWNLOAD_DATA")) {
            showMessage("Download Data...")
            val workRequest = OneTimeWorkRequestBuilder<DownloadDataWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .setInputData(
                    workDataOf(
                        "uid" to uid
                    )
                )
                .build()

            workManager.enqueueUniqueWork(
                "DOWNLOAD_DATA",
                ExistingWorkPolicy.KEEP,
                workRequest
            )
//        } else {
//            showMessage("Download Data...")
//        }
    }

}