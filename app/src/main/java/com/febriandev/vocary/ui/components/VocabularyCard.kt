package com.febriandev.vocary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febriandev.vocary.R
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


                        IconButton(onClick = onShareClick) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

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

                if (shouldCaptureScreenshot) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),

                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.icon_vocary),
                                        contentDescription = stringResource(R.string.app_name),
                                        modifier = Modifier
                                            .size(16.dp),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Vocary",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
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
