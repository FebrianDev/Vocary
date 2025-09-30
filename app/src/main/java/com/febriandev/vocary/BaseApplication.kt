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

        Purchases.configure(
            PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_APP_ID)
                .build()
        )
        
        OneSignal.initWithContext(this, BuildConfig.ONESIGNAL_APP_ID)
        OneSignal.Notifications.addClickListener(object : INotificationClickListener {

            override fun onClick(event: INotificationClickEvent) {

                val notification = event.notification
                val additionalData = notification.additionalData

                val vocabId = additionalData?.optString("vocab_id")

                val intent = Intent(this@BaseApplication, MainActivity::class.java).apply {
                    putExtra("VOCAB_ID", vocabId)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        })

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}