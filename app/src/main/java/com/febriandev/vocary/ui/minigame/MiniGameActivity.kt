package com.febriandev.vocary.ui.minigame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.febriandev.vocary.domain.Question
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MiniGameActivity : ComponentActivity() {

//    private val viewModel: MiniGameViewModel by viewModels {
//        MiniGameViewModelFactory(/* repo bisa diinject di sini */ FakeGameSessionRepository())
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MiniGameScreen()
            }
        }
    }
}

@Composable
fun MiniGameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    defaultTotalQuestions: Int = 5
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        // auto-start; kalau mau manual, hapus ini dan tampilkan tombol Start
        viewModel.startSession(defaultTotalQuestions)
    }

    when {
        state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(state.error ?: "", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { viewModel.startSession(defaultTotalQuestions) }) {
                    Text("Mulai Lagi")
                }
            }
        }

        state.finished -> SessionResultView(
            score = state.score,
            correct = state.correct,
            wrong = state.wrong,
            total = state.totalQuestions,
            onRestart = { viewModel.startSession(defaultTotalQuestions) }
        )

        else -> QuestionView(
            index = state.index,
            total = state.totalQuestions,
            question = state.current,
            selected = state.selected,
            answered = state.answered,
            onSelect = { viewModel.submitAnswer(it) },
            onNext = { viewModel.next() }
        )
    }
}

@Composable
private fun QuestionView(
    index: Int,
    total: Int,
    question: Question?,
    selected: String?,
    answered: Boolean,
    onSelect: (String) -> Unit,
    onNext: () -> Unit
) {
    if (question == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Menyiapkan soal…")
        }
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LinearProgressIndicator(
            progress = { (index + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Text("Soal ${index + 1} dari $total", style = MaterialTheme.typography.labelLarge)
        Text(question.prompt, style = MaterialTheme.typography.titleMedium)

        question.options.forEach { option ->
            val isCorrect = option == question.correctAnswer
            val isSelected = option == selected

            val border = when {
                answered && isCorrect -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                answered && isSelected && !isCorrect -> BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.error
                )

                else -> null
            }

            OutlinedButton(
                onClick = { if (!answered) onSelect(option) },
                modifier = Modifier.fillMaxWidth(),
                border = border
            ) {
                Text(option)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                if (answered) {
                    if (selected == question.correctAnswer) "Benar!" else "Salah. Jawaban: ${question.correctAnswer}"
                } else "Pilih jawaban…",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = onNext,
                enabled = answered
            ) { Text("Lanjut") }
        }
    }
}

@Composable
private fun SessionResultView(
    score: Int,
    correct: Int,
    wrong: Int,
    total: Int,
    onRestart: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Selesai!", style = MaterialTheme.typography.titleLarge)
            Text("Skor: $score%", style = MaterialTheme.typography.titleMedium)
            Text("Benar: $correct • Salah: $wrong • Total: $total")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRestart) { Text("Main Lagi") }
        }
    }
}
