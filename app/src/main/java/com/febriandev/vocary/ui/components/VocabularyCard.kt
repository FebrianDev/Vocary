package com.febriandev.vocary.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febriandev.vocary.data.db.entity.SrsStatus

@Composable
fun VocabularyCard(
    word: String,
    phonetic: String,
    partOfSpeech: String,
    definition: String,
    example: String,
    isFavorite: Boolean,
    srsStatus: SrsStatus,
    shouldCaptureScreenshot: Boolean,
    active: Boolean = true,
    onPlayPronunciationClick: () -> Unit,
    onInfoClick: () -> Unit,
    onShareClick: () -> Unit,
    onNotes: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAnswerClick: (SrsStatus) -> Unit
) {
    val background = MaterialTheme.colorScheme.surface
    val primary = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = background,
            tonalElevation = 4.dp,
            // border = if(shouldCaptureScreenshot) BorderStroke(1.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Word & Favorite
                Text(
                    text = word,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (word.length > 10) 36.sp else 42.sp
                    ),
                    color = primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    shadowElevation = 4.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phonetic,
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onPlayPronunciationClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Play",
                                tint = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "${if (partOfSpeech.isNotEmpty()) "${(partOfSpeech)} " else ""}$definition",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (example.isNotEmpty()) {
                    Text(
                        text = "Example: $example",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!shouldCaptureScreenshot) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onInfoClick) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(onClick = onNotes) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Notes",
                                tint = primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

//                    IconButton(onClick = onTranslate) {
//                        Icon(
//                            Icons.Default.Translate,
//                            contentDescription = "Translate",
//                            tint = primary,
//                            modifier = Modifier.size(32.dp)
//                        )
//                    }


                        //  Spacer(modifier = Modifier.width(24.dp))

                        IconButton(onClick = onShareClick) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }


                        // Spacer(modifier = Modifier.width(32.dp))

                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!shouldCaptureScreenshot) {
            if (active) {
                VocabularyAnswerSection(srsStatus, onAnswerClick)
            }
        }
    }
}
