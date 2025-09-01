package com.febriandev.vocary.ui.streaks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.StreakDay
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.repository.StreakRepository
import com.febriandev.vocary.utils.Constant.DAILY_GOAL
import com.febriandev.vocary.utils.Constant.OPEN_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StreakViewModel @Inject constructor(private val repository: StreakRepository) : ViewModel() {

    private val _appOpenStreak = MutableStateFlow<List<StreakDay>>(emptyList())
    val appOpenStreak: StateFlow<List<StreakDay>> = _appOpenStreak

    private val _showAppOpenStreak = MutableStateFlow(false)
    val showAppOpenStreak: StateFlow<Boolean> = _showAppOpenStreak

    private val _dailyGoalStreak = MutableStateFlow<List<StreakDay>>(emptyList())
    val dailyGoalStreak: StateFlow<List<StreakDay>> = _dailyGoalStreak

    private val _showDailyGoalStreak = MutableStateFlow(false)
    val showDailyGoalStreak: StateFlow<Boolean> = _showDailyGoalStreak

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStreaks() {
        viewModelScope.launch {
            _appOpenStreak.value = repository.getStreaksByType("OPEN_APP")
            _dailyGoalStreak.value = repository.getStreaksByType("DAILY_GOAL")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun markStreak(type: String, completed: Boolean) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val existing = repository.getStreakByDate(type, today)

         //   if (existing == null) {
                val streak = StreakEntity(
                    streakType = type,
                    date = today,
                    isCompleted = completed
                )
                repository.insertStreak(streak)
                loadStreaks()

                when (type) {
                    OPEN_APP -> _showAppOpenStreak.value = true
                    DAILY_GOAL -> _showDailyGoalStreak.value = true
                }
          //  }
        }
    }

    fun dismissAppOpenSheet() {
        _showAppOpenStreak.value = false
    }

    fun dismissDailyGoalSheet() {
        _showDailyGoalStreak.value = false
    }

}