package com.febriandev.vocary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.data.db.entity.SrsStatus

@Composable
fun VocabularyAnswerSection(
    srsStatus: SrsStatus,
    onAnswerClick: (SrsStatus) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnswerButton(
            text = "Nope",
            backgroundColor = if (srsStatus == SrsStatus.NOPE) Color(0xFFD62828) else Color(
                0xFFEE6C4D
            ),
            isSelected = srsStatus == SrsStatus.NOPE,
            modifier = Modifier.weight(1f)
        ) {
            onAnswerClick(if (srsStatus == SrsStatus.NOPE) SrsStatus.NEW else SrsStatus.NOPE)
        }

        AnswerButton(
            text = "Maybe",
            backgroundColor = if (srsStatus == SrsStatus.MAYBE) Color(0xFFF4A261) else Color(
                0xFFE9C46A
            ),
            isSelected = srsStatus == SrsStatus.MAYBE,
            modifier = Modifier.weight(1f)
        ) {
            onAnswerClick(if (srsStatus == SrsStatus.MAYBE) SrsStatus.NEW else SrsStatus.MAYBE)
        }

        AnswerButton(
            text = "Known",
            backgroundColor = if (srsStatus == SrsStatus.KNOWN) Color(0xFF264653) else Color(
                0xFF2A9D8F
            ),
            isSelected = srsStatus == SrsStatus.KNOWN,
            modifier = Modifier.weight(1f)
        ) {
            onAnswerClick(if (srsStatus == SrsStatus.KNOWN) SrsStatus.NEW else SrsStatus.KNOWN)
        }
    }
}

