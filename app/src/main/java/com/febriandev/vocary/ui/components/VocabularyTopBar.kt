package com.febriandev.vocary.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Crown
import com.composables.icons.lucide.Lucide
import com.febriandev.vocary.R

@Composable
fun VocabularyTopBar(
    isPremium: Boolean = false,
    name: String,
    streakDays: Int,
    todayCount: Int,
    dailyGoal: Int,
    xp: Int
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            // LEFT: Name and Badge Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPremium) {
                    Image(
                        painterResource(R.drawable.ic_crown),
                        contentDescription = "Crown",
                        modifier = Modifier.size(42.dp)
                    )
                } else {
                    Icon(
                        imageVector = Lucide.Crown,
                        contentDescription = "Crown",
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp).size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(0.45f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            XpPulse(xp)
        }

        // RIGHT: Streak and Today Progress
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PulsingFireIcon() // icon api lebih besar
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$streakDays Days Streak",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            AnimatedProgressIndicator(
                todayCount = todayCount,
                dailyGoal = dailyGoal,
            )
        }
    }
}

@Composable
fun AnimatedProgressIndicator(
    todayCount: Int,
    dailyGoal: Int
) {
    val targetProgress = (todayCount.toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnim"
    )

    // animasi pulse agar tetap hidup
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        LinearProgressIndicator(
            progress = animatedProgress, // pakai yang animasi
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .width(100.dp)
                .height(8.dp)
                .scale(scale)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$todayCount/$dailyGoal",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}


@Composable
fun PulsingFireIcon() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Icon(
        imageVector = Icons.Default.LocalFireDepartment,
        contentDescription = "Streak",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(24.dp)
            .scale(scale)
    )
}

@Composable
fun XpPulse(xp: Int) {
    val colorScheme = MaterialTheme.colorScheme

    // Animasi scale pulse
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = "XP: $xp",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.primary
            ),
            modifier = Modifier.scale(scale)
        )
    }
}