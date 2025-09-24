package com.febriandev.vocary.utils

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DialogOK(
    showDialog: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                Text(
                    text = "Ok",
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        )
    }
}

@Composable
fun DialogYesNo(
    showDialog: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit),
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                Text(
                    text = "Yes",
                    modifier = Modifier.clickable {
                        onConfirm.invoke()
                    }
                )
            },
            dismissButton = {
                Text(
                    text = "No",
                    modifier = Modifier.clickable {
                        onDismiss.invoke()
                    }
                )
            }
        )
    }
}

