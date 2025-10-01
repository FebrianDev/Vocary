package com.febriandev.vocary.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.febriandev.vocary.data.repository.UserRepository
import com.febriandev.vocary.data.repository.VocabularyRepository
import com.febriandev.vocary.utils.Constant.NOTIFICATION
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.sendOneSignalNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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
        val vocabulary = vocabularyRepository.getAllVocabulary(System.currentTimeMillis())
        val vocab = if(vocabulary.isEmpty()) null else vocabulary[0]

        if (currentHour in 7..20 && Prefs[NOTIFICATION, true]) {

            val definition = vocab?.definition
            if (vocab != null) {
                if (definition != null) {
                    sendOneSignalNotification(
                        userRepository.getCurrentUser()?.id.toString(),
                        vocab.id,
                        vocab.word,
                        "(${vocab.partOfSpeech}) $definition",
                    )
                }
            }

        }
        return Result.success()
    }
}