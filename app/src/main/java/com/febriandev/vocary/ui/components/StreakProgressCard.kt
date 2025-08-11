package com.febriandev.vocary.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StreakProgressCard(currentStreak: Int, targetStreak: Int = 10) {
    val progress = currentStreak / targetStreak.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE0F7F5)) // Light greenish background seperti gambar
            .padding(16.dp)
    ) {

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007A5E)
                    )
                ) {
                    append("$currentStreak")
                }
                append(" of ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$targetStreak")
                }
                append(" days streak")
            },
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = progress,
            color = Color(0xFF007A5E),
            trackColor = Color(0xFFCDEDE4), // Material3: 'trackColor' bukan 'backgroundColor'
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
        )

    }
}
