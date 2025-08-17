package com.febriandev.vocary.ui.minigame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.GameSessionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.data.repository.GameRepository
import com.febriandev.vocary.domain.Question
import com.febriandev.vocary.domain.generateQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {


    data class UiState(
        val loading: Boolean = false,
        val sessionId: String? = null,
        val startedAt: Long = 0L,
        val finished: Boolean = false,
        val totalQuestions: Int = 10,
        val index: Int = 0,
        val current: Question? = null,
        val selected: String? = null,
        val answered: Boolean = false,
        val correct: Int = 0,
        val wrong: Int = 0,
        val score: Int = 0,
        val error: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private var vocabPool: List<VocabularyEntity> = emptyList()
    private val usedVocabIds = mutableSetOf<String>()

    fun startSession(totalQuestions: Int = 10) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            vocabPool = repository.getAllVocabulary()
            if (vocabPool.size < 4) {
                uiState = uiState.copy(
                    loading = false,
                    error = "Data vocabulary tidak cukup untuk membuat soal (min. 4 dengan definisi)."
                )
                return@launch
            }

            usedVocabIds.clear()
            val sessionId = UUID.randomUUID().toString()
            uiState = UiState(
                loading = false,
                sessionId = sessionId,
                startedAt = System.currentTimeMillis(),
                totalQuestions = totalQuestions
            )
            loadNextQuestion()
        }
    }

    private fun loadNextQuestion() {
        val freshPool = vocabPool.filter { it.id !in usedVocabIds }
        val base = if (freshPool.size >= 4) freshPool else vocabPool
        val q = generateQuestion(base)
        if (q == null) {
            uiState = uiState.copy(error = "Gagal membuat soal. Coba mulai sesi baru.")
            return
        }
        usedVocabIds.add(q.vocabId)
        uiState = uiState.copy(
            current = q,
            selected = null,
            answered = false
        )
    }

    fun submitAnswer(option: String) {
        val q = uiState.current ?: return
        if (uiState.answered) return

        val correct = option == q.correctAnswer
        uiState = uiState.copy(
            selected = option,
            answered = true,
            correct = uiState.correct + if (correct) 1 else 0,
            wrong = uiState.wrong + if (correct) 0 else 1
        )
    }

    fun next() {
        if (!uiState.answered) return
        val nextIndex = uiState.index + 1
        if (nextIndex >= uiState.totalQuestions) {
            finishSession()
        } else {
            uiState = uiState.copy(index = nextIndex)
            loadNextQuestion()
        }
    }

    private fun finishSession() {
        val end = System.currentTimeMillis()
        val scorePercent = ((uiState.correct * 100.0) / uiState.totalQuestions).toInt()
        val session = GameSessionEntity(
            sessionId = uiState.sessionId ?: UUID.randomUUID().toString(),
            startTime = uiState.startedAt,
            endTime = end,
            totalQuestions = uiState.totalQuestions,
            correctAnswers = uiState.correct,
            wrongAnswers = uiState.wrong,
            score = scorePercent
        )

        viewModelScope.launch {
            repository.insertSession(session)
        }

        uiState = uiState.copy(
            finished = true,
            score = scorePercent
        )
    }
}
