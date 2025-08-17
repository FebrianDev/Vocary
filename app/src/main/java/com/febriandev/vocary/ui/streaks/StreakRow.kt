package com.febriandev.vocary.ui.streaks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StreakRow(
    title: String,
    streakDays: List<StreakDay>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            streakDays.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                         //   .padding(8.dp)
                            .background(
                                if (day.isCompleted) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayName.take(3),
                            color = if (day.isCompleted) Color.White else Color.Black
                        )
                    }
                    if (day.isCompleted) {
                        Text("🔥", fontSize = 18.sp)
                    } else {
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakBottomSheet(
    appOpenStreak: List<StreakDay>,
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
            Text("🔥 Your Streaks", style = MaterialTheme.typography.titleLarge)

            StreakRow(title = "App Open Streak", streakDays = appOpenStreak)
            StreakRow(title = "Daily Goal Streak", streakDays = goalStreak)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen() {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(true) }

    val appOpenStreak = listOf(
        StreakDay("Mon", true),
        StreakDay("Tue", true),
        StreakDay("Wed", false),
        StreakDay("Thu", false),
        StreakDay("Fri", false),
        StreakDay("Sat", false),
        StreakDay("Sun", false),
    )

    val goalStreak = listOf(
        StreakDay("Mon", true),
        StreakDay("Tue", false),
        StreakDay("Wed", false),
        StreakDay("Thu", false),
        StreakDay("Fri", false),
        StreakDay("Sat", false),
        StreakDay("Sun", false),
    )

    if (showSheet) {
        StreakBottomSheet(
            appOpenStreak = appOpenStreak,
            goalStreak = goalStreak,
            sheetState = sheetState,
            onDismiss = { showSheet = false }
        )
    }
}

data class StreakDay(
    val dayName: String,
    val isCompleted: Boolean
)