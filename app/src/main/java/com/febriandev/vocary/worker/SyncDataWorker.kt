package com.febriandev.vocary.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.febriandev.vocary.data.repository.SyncDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val repository: SyncDataRepository
) : CoroutineWorker(appContext, workerParams) {

    private val channelId = "sync_channel"

    override suspend fun doWork(): Result {
        createNotificationChannel(channelId)

        // Tampilkan notifikasi indeterminate saat sync
        setForeground(createForegroundInfo("Syncing data..."))

        val userId = workerParams.inputData.getString("uid")

        return try {
            if (userId != null) {
                repository.syncAll(userId)
            }// jalankan semua sync tanpa progress detail

            showCompletedNotification("All data synced successfully")
            Result.success()
        } catch (e: Exception) {
            showCompletedNotification("Data sync failed")
            Result.retry()
        }
    }

    private fun createForegroundInfo(progressText: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Data Sync")
            .setContentText(progressText)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setOngoing(true)
            .setProgress(0, 0, true) // <-- indeterminate progress bar
            .build()

        return ForegroundInfo(1, notification)
    }

    private fun showCompletedNotification(message: String) {
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Data Sync")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)
            .setAutoCancel(true)
            .build()

        val nm =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(2, notification)
    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Data Sync",
                NotificationManager.IMPORTANCE_LOW
            )
            val nm =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }
}

