package com.febriandev.vocary.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.DailyProgressEntity
import com.febriandev.vocary.data.repository.DailyProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val dailyProgressRepository: DailyProgressRepository
) : ViewModel() {

//    fun onAppOpen() = viewModelScope.launch {
//        val today = todayDate()
//        val streak = userStreakDao.getStreak() ?: UserStreak()
//
//        val updated = if (streak.lastAppOpenDate == today) {
//            streak
//        } else if (isYesterday(streak.lastAppOpenDate, today)) {
//            streak.copy(
//                appOpenStreak = streak.appOpenStreak + 1,
//                lastAppOpenDate = today
//            )
//        } else {
//            streak.copy(appOpenStreak = 1, lastAppOpenDate = today)
//        }
//
//        userStreakDao.insertOrUpdate(updated)
//    }

    private val _dailyProgress = MutableStateFlow<DailyProgressEntity?>(null)
    val dailyProgress: StateFlow<DailyProgressEntity?> = _dailyProgress


    fun onVocabularyKnown(vocabId: String, target: Int = 10) = viewModelScope.launch {
        val today = todayDate()
        val progress = dailyProgressRepository.getProgress(today)

        if (progress != null) {

            val alreadyAdded = progress.listVocabulary.contains(vocabId)

            val updatedProgress = if (!alreadyAdded) {
                progress.copy(
                    progress = progress.progress + 1,
                    isGoalAchieved = (progress.progress + 1) >= target,
                    listVocabulary = progress.listVocabulary + vocabId, // tambahkan vocab baru
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                progress
            }

            dailyProgressRepository.update(updatedProgress)
            getProgress()

        } else {
            dailyProgressRepository.insert(
                DailyProgressEntity(
                    today,
                    1,
                    listVocabulary = listOf(vocabId)
                ),
            )
            getProgress()
        }
    }

    fun getProgress() = viewModelScope.launch {
        val today = todayDate()

        _dailyProgress.value = dailyProgressRepository.getProgress(today)
    }

//    private suspend fun updateGoalStreak(today: String) {
//        val streak = userStreakDao.getStreak() ?: UserStreak()
//
//        val updated = if (streak.lastGoalDate == today) {
//            streak
//        } else if (isYesterday(streak.lastGoalDate, today)) {
//            streak.copy(
//                goalStreak = streak.goalStreak + 1,
//                lastGoalDate = today
//            )
//        } else {
//            streak.copy(goalStreak = 1, lastGoalDate = today)
//        }
//
//        userStreakDao.insertOrUpdate(updated)
//    }

    private fun todayDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
        Date()
    )

    private fun isYesterday(lastDate: String, today: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val last = sdf.parse(lastDate) ?: return false
        val now = sdf.parse(today) ?: return false

        val diff = (now.time - last.time) / (1000 * 60 * 60 * 24)
        return diff == 1L
    }
}
