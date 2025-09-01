package com.febriandev.vocary.ui.streaks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.febriandev.vocary.data.db.entity.StreakDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen(viewModel: StreakViewModel = hiltViewModel()) {
    val appOpenStreak by viewModel.appOpenStreak.collectAsState()
    val dailyGoalStreak by viewModel.dailyGoalStreak.collectAsState()

    val showAppOpenSheet by viewModel.showAppOpenStreak.collectAsState()
    val showDailyGoalSheet by viewModel.showDailyGoalStreak.collectAsState()

    if (showAppOpenSheet) {
        val week = appOpenStreak.firstOrNull()?.week ?: 1
        FloatingAnimatedCard(
            onDismiss = { viewModel.dismissAppOpenSheet() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //  Text("$week 🔥 App Open Streak", style = MaterialTheme.typography.titleLarge)
                Text("App Open Streak", style = MaterialTheme.typography.titleMedium)
                StreakRow(streakDays = appOpenStreak)
            }
        }
    }

    if (showDailyGoalSheet) {
        FloatingAnimatedCard(
            onDismiss = { viewModel.dismissDailyGoalSheet() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //  Text("$week 🔥 App Open Streak", style = MaterialTheme.typography.titleLarge)
                Text("🔥 Daily Goal Streak", style = MaterialTheme.typography.titleMedium)
                StreakRow(streakDays = dailyGoalStreak)
            }
        }
    }
}

@Composable
fun FloatingAnimatedCard(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var dismissedByClick by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        visible = true
        delay(4000)
        if (!dismissedByClick) {
            visible = false
            delay(1000)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            // mulai dari -100 (atas) lalu turun ke 0
            initialOffsetY = { fullHeight -> -fullHeight / 2 },
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        ) + scaleIn(
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            initialScale = 0.8f
        ) + fadeIn(
            animationSpec = tween(durationMillis = 1000)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight / 2 },
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        ) + scaleOut(
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            targetScale = 0.8f
        ) + fadeOut(
            animationSpec = tween(durationMillis = 1000)
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    dismissedByClick = true
                    visible = false
                    scope.launch {
                        delay(1000)
                        onDismiss()
                    }
                }
        ) {
            content()
        }
    }
}


@Composable
fun StreakRow(
    streakDays: List<StreakDay>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            streakDays.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (day.isCompleted) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (day.isCompleted) 0.dp else 1.dp,
                                color = if (day.isCompleted) Color.Transparent
                                else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayName.take(2).uppercase(),
                            color = if (day.isCompleted) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (day.isCompleted) MaterialTheme.colorScheme.primary/* api warna orange */
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (day.isCompleted) Icons.Default.Whatshot
                            else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (day.isCompleted) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOpenStreakBottomSheet(
    appOpenStreak: List<StreakDay>,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {

    val week = appOpenStreak.firstOrNull()?.week ?: 1

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$week 🔥 App Open Streak", style = MaterialTheme.typography.titleLarge)
            StreakRow(streakDays = appOpenStreak)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyGoalStreakBottomSheet(
    goalStreak: List<StreakDay>,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("🔥 Daily Goal Streak", style = MaterialTheme.typography.titleLarge)
            StreakRow(streakDays = goalStreak)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}