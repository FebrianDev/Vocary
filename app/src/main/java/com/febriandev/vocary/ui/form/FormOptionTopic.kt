package com.febriandev.vocary.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.onboard.TopicType

@Composable
fun FormOptionTopic(
    topicType: TopicType,
    isSelected: Boolean = false,
    onOptionSelected: (topicType: TopicType) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onOptionSelected(topicType) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = topicType.displayName,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .padding(2.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, shape = CircleShape)
                    .size(20.dp)
                    .background(Color.Transparent, shape = CircleShape)
                    .padding(2.dp)
            )
        }
    }
}