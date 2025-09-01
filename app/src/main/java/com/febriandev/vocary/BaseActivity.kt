package com.febriandev.vocary

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.febriandev.vocary.utils.ConnHelper
import com.febriandev.vocary.worker.GenerateVocabWorker
import com.febriandev.vocary.worker.ProcessVocabWorker

abstract class BaseActivity : ComponentActivity() {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(applicationContext)

    private fun isWorkerRunning(uniqueName: String): Boolean {
        val workInfoList = workManager.getWorkInfosForUniqueWork(uniqueName).get()
        return workInfoList.any {
            it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
        }
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

        val processRequest = OneTimeWorkRequestBuilder<ProcessVocabWorker>().addTag("ProcessVocab").build()
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
}