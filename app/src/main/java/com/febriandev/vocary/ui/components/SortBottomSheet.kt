package com.febriandev.vocary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    sortType: SortType,
    sortOrder: SortOrder,
    onSortChange: (SortType, SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    val types = listOf(SortType.NAME to "Name", SortType.DATE to "Date")
    val orders = listOf(SortOrder.ASCENDING to "Ascending", SortOrder.DESCENDING to "Descending")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 72.dp)) {
            Text("Sort By", style = MaterialTheme.typography.titleLarge)

            types.forEach { (type, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortChange(type, sortOrder) }
                ) {
                    RadioButton(
                        selected = type == sortType,
                        onClick = {
                            onSortChange(type, sortOrder)
                        })
                    Text(label, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Order", style = MaterialTheme.typography.titleLarge)

            orders.forEach { (order, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortChange(sortType, order) }
                ) {
                    RadioButton(selected = order == sortOrder, onClick = {
                        onSortChange(sortType, order)
                    })
                    Text(label, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

enum class SortType {
    NAME, DATE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}