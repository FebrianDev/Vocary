package com.febriandev.vocary

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.febriandev.vocary.ui.theme.ThemeState
import com.febriandev.vocary.utils.Constant.DARK_MODE
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.worker.WorkFactory
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