package com.febriandev.vocary.ui.minigame

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.febriandev.vocary.domain.Question
import com.febriandev.vocary.ui.theme.VocaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MiniGameActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VocaryTheme {
                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { _ ->
                    MiniGameScreen {
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun MiniGameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    defaultTotalQuestions: Int = 2,
    onFinish: () -> Unit
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
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
                    Text("Play Again!")
                }
            }
        }

        state.finished -> SessionResultView(
            score = state.score,
            correct = state.correct,
            wrong = state.wrong,
            total = state.totalQuestions,
            onRestart = { viewModel.startSession(defaultTotalQuestions) },
            onFinish = onFinish
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
            Text("Preparing question…")
        }
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LinearProgressIndicator(
            progress = { (index + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Text("Question ${index + 1} of $total", style = MaterialTheme.typography.labelLarge)
        Text(question.prompt, style = MaterialTheme.typography.titleMedium)

        question.options.forEach { option ->
            val isCorrect = option == question.correctAnswer
            val isSelected = option == selected

            val border = when {
                answered && isCorrect -> BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        option,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        }

        Spacer(Modifier.height(8.dp))

        Column(Modifier.fillMaxWidth()) {
            Text(
                if (answered) {
                    if (selected == question.correctAnswer) "Correct!" else "Wrong. Answer: ${question.correctAnswer}"
                } else "Choose an answer…",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNext,
                enabled = answered
            ) { Text("Next") }
        }
    }
}

@Composable
private fun SessionResultView(
    score: Int,
    correct: Int,
    wrong: Int,
    total: Int,
    onRestart: () -> Unit,
    onFinish: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Finished!", style = MaterialTheme.typography.titleLarge)
            Text("Score: $score%", style = MaterialTheme.typography.titleMedium)
            Text("Correct: $correct • Wrong: $wrong • Total: $total")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) { Text("Play Again") }
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) { Text("Exit") }
        }
    }
}
