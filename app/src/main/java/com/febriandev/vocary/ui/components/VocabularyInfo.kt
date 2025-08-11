package com.febriandev.vocary.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.domain.Vocabulary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyInfo(
    showSheet: Boolean,
    selectedVocab: Vocabulary?,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit
) {
    if (showSheet && selectedVocab != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        "Word: ${selectedVocab.word}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        "Phonetic: ${selectedVocab.phonetic}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        "Part of Speech: ${selectedVocab.partOfSpeech}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Definition:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        selectedVocab.definition,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (selectedVocab.examples.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Examples:", style = MaterialTheme.typography.labelLarge)
                        selectedVocab.examples.forEach {
                            Text("• $it", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    if (selectedVocab.synonyms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Synonyms:", style = MaterialTheme.typography.labelLarge)
                        Text(
                            selectedVocab.synonyms.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedVocab.sourceUrl.isNotBlank()) {
                        Text(
                            "Source: ${selectedVocab.sourceUrl}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
