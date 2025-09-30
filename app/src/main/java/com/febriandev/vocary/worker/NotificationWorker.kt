package com.febriandev.vocary.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.febriandev.vocary.MainActivity
import com.febriandev.vocary.data.repository.UserRepository
import com.febriandev.vocary.data.repository.VocabularyRepository
import com.febriandev.vocary.utils.Constant.NOTIFICATION
import com.febriandev.vocary.utils.Prefs
import com.onesignal.OneSignal
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    @Assisted private val vocabularyRepository: VocabularyRepository,
    @Assisted private val userRepository: UserRepository

) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val vocab = vocabularyRepository.getVocab()

        // Hanya tampilkan notifikasi antara jam 07.00 sampai 20.00
        //if (currentHour in 7..20 && Prefs[NOTIFICATION, true]) {

            val definition = vocab?.definition
            if (vocab != null) {
                if (definition != null) {
                    sendOneSignalNotification(
                        vocab.word,
                        "(${vocab.partOfSpeech}) $definition",
                        userRepository.getCurrentUser()?.id.toString()
                    )

                    Log.d("UserId", userRepository.getCurrentUser()?.id.toString())
                }
            }

        Log.d("UserId", userRepository.getCurrentUser()?.id.toString())
      //  }

        return Result.success()
    }

    private fun sendOneSignalNotification(userId: String, title: String, message: String) {

        val oneSignalAppId = "0140f446-57b5-4908-a124-5b6f55e0499e"
        val restApiKey =
            "os_v2_app_afapirsxwveqrijelnxvlycjtzhycsbxx3beyxufgml2fpjghldktyupims7q24etc3kfkxtx4nozfhhe5wd5dglbp7e7lgslz7tafi"

        Log.d("OneSignal2", "Current externalId: ${userId}")


        // JSON payload menggunakan external user ID
        val jsonBody = JSONObject().apply {
            put("app_id", oneSignalAppId)
            put("include_external_user_ids", JSONArray().put(userId))
            put("headings", JSONObject().put("en", title))
            put("contents", JSONObject().put("en", message))
        }

        try {
            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonBody.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://onesignal.com/api/v1/notifications")
                .post(body)
                .addHeader("Authorization", "Basic $restApiKey")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(
                        "NotificationWorker",
                        "Failed to send OneSignal notification: ${response.code}"
                    )
                } else {
                    Log.d("NotificationWorker", "Notification sent successfully")
                }
            }
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error sending OneSignal notification", e)
        }
    }


}