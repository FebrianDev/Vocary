package com.febriandev.vocary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyWordProgress(
    learned: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progress = learned.toFloat() / total

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE0F7F5)) // Light greenish background seperti gambar
            .padding(16.dp)
    ) {
        // Highlighted text
        val primaryColor = Color(0xFF007E7E) // Dark green like image

        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
                    append("$learned")
                }
                append(" of ")
                withStyle(SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
                    append("$total")
                }
                append(" words learned today")
            },
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFC8E9E6)) // track color
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(50))
                    .background(primaryColor)
            )
        }
    }
}

