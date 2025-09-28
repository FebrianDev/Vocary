package com.febriandev.vocary

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.work.Configuration
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.worker.WorkFactory
import com.onesignal.OneSignal
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkFactory

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        //  ThemeState.themeMode.value = Prefs[DARK_MODE, ThemeState.themeMode.value]

        Purchases.configure(
            PurchasesConfiguration.Builder(this, "goog_SDrghgQzcdXSqrZMAYWTJQzvGZn")
                .build()
        )

        val oneSignalAppId = "0140f446-57b5-4908-a124-5b6f55e0499e"

        // Init OneSignal v5
        OneSignal.initWithContext(this, oneSignalAppId)

        // Debug log
        // OneSignal.Debug.logLevel = Log.VERBOSE

        // Listener kalau notif dibuka user
        OneSignal.Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val notification = event.notification
                val additionalData = notification.additionalData

                Log.d("OneSignal", "Notif clicked: $additionalData")

                val intent = Intent(this@BaseApplication, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        })

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            // App masuk background
//            val constraints = Constraints.Builder()
//                .setRequiresBatteryNotLow(true)
//                .setRequiresDeviceIdle(false) // ubah true jika kamu ingin hanya saat idle
//                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
//                .build()
//
//            val request = PeriodicWorkRequestBuilder<NotificationWorker>(4, TimeUnit.HOURS)
//                .setConstraints(constraints)
//                .setInitialDelay(4, TimeUnit.HOURS)
//                .build()
//
//            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//                "daily_notification",
//                ExistingPeriodicWorkPolicy.UPDATE, // agar tidak dijalankan berulang
//                request
//            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}