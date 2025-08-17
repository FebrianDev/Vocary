package com.febriandev.vocary.ui.streaks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.StreakEntity
import com.febriandev.vocary.data.repository.StreakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StreakViewModel @Inject constructor(private val repository: StreakRepository) : ViewModel() {

    private val _appOpenStreak = MutableStateFlow<List<StreakEntity>>(emptyList())
    val appOpenStreak: StateFlow<List<StreakEntity>> = _appOpenStreak

    private val _dailyGoalStreak = MutableStateFlow<List<StreakEntity>>(emptyList())
    val dailyGoalStreak: StateFlow<List<StreakEntity>> = _dailyGoalStreak

    fun loadStreaks() {
        viewModelScope.launch {
            _appOpenStreak.value = repository.getStreaksByType("APP_OPEN")
            _dailyGoalStreak.value = repository.getStreaksByType("DAILY_GOAL")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun markStreak(type: String, completed: Boolean) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val streak = StreakEntity(
                streakType = type,
                date = today,
                isCompleted = completed
            )
            repository.insertStreak(streak)
            loadStreaks()
        }
    }
}