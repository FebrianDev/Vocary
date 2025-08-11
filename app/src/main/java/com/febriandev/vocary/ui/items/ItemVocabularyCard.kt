package com.febriandev.vocary.ui.items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemVocabularyCard(
    word: String,
    phonetic: String,
    definition: String,
    timestamp: Long,
    onPlayPronunciationClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), // Dark background
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = word,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        //  color = Color.White
                    )
                )

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.clickable { onPlayPronunciationClick.invoke() }) {
//                    Text(
//                        text = phonetic,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    Icon(
//                        imageVector = Icons.Default.VolumeUp,
//                        contentDescription = "Play pronunciation",
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .size(20.dp)
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = definition,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = timestampToDate(timestamp / 1000),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun timestampToDate(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.US)
    return dateTime.format(formatter)
}