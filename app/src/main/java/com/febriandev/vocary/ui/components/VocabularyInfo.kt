package com.febriandev.vocary.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.utils.showMessage
import com.google.android.gms.common.wrappers.Wrappers.packageManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyInfo(
    showSheet: Boolean,
    selectedVocab: Vocabulary?,
    context: Context,
    onDismiss: () -> Unit
) {

    if (selectedVocab == null) return

    CustomAnimatedModalSheet2(
        title = "Info",
        show = showSheet,
        onDismiss = onDismiss
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Word Info
                Text(
                    selectedVocab.word,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "(${selectedVocab.phonetic})",
                    style = MaterialTheme.typography.titleMedium
                )

                // Definitions
                Text(
                    text = "Definitions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                selectedVocab.definitions.forEach { definition ->

                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = "Part of Speech:",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = definition.partOfSpeech ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }

                    Text(
                        text = "Definition:",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "- ${definition.definition}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                    )

                    // Examples
                    if (definition.examples.isNotEmpty()) {
                        Text(
                            text = "Examples:",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        definition.examples.forEach { example ->
                            Text(
                                text = "- $example",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }

                    // Synonyms
                    if (definition.synonyms.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = "Synonyms:",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = definition.synonyms.joinToString(", "),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                // Source
                if (selectedVocab.sourceUrl.isNotBlank()) {
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            "Source: ", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            text = selectedVocab.sourceUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    selectedVocab.sourceUrl.let { url ->
                                        try {
                                            val cleanUrl = url.trim()
                                            val uri = Uri.parse(cleanUrl)

                                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                                addCategory(Intent.CATEGORY_BROWSABLE)
                                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "Open with"))
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            context.showMessage("Can't open link!")
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }

    }
}
