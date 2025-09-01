package com.febriandev.vocary.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.auth.AuthActivity
import com.febriandev.vocary.ui.components.CustomAnimatedModalSheet
import com.febriandev.vocary.ui.theme.ThemeState
import com.febriandev.vocary.utils.Constant.DARK_MODE
import com.febriandev.vocary.utils.Constant.NOTIFICATION
import com.febriandev.vocary.utils.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    showProfile: Boolean,
    scope: CoroutineScope,
    context: Context,
    onDeleteData: () -> Unit,
    onDismiss: () -> Unit
) {

    var isCheckedNotification by remember { mutableStateOf(Prefs[NOTIFICATION, true]) }

    var isAbout by remember { mutableStateOf(false) }
    var isLogout by remember { mutableStateOf(false) }
    var logout by remember {
        mutableStateOf(false)
    }

    CustomAnimatedModalSheet(show = showProfile, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Setting",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {

                Column {
                    SettingsRow(
                        icon = Icons.Default.Person,
                        title = "Profile",
                        modifier = Modifier.padding(vertical = 12.dp),
                        onClick = { }
                    )

                    SettingsRow(
                        icon = Icons.Default.Brightness4,
                        title = "Dark Mode",
                        switchChecked = ThemeState.isDarkMode.value,
                        onSwitchChange = {
                            ThemeState.isDarkMode.value = it   // trigger recompose
                            Prefs[DARK_MODE] = it
                        }
                    )

                    SettingsRow(
                        icon = Icons.Default.Notifications,
                        title = "Notification",
                        switchChecked = isCheckedNotification,
                        onSwitchChange = {
                            isCheckedNotification = it
                            Prefs[NOTIFICATION] = it
                        }
                    )

                    SettingsRow(
                        icon = Icons.Default.Settings,
                        title = "Sync Data to Server",
                        modifier = Modifier.padding(vertical = 12.dp),
                        onClick = { }
                    )

                    SettingsRow(
                        icon = Icons.Default.Info,
                        title = "About",
                        modifier = Modifier.padding(vertical = 12.dp),
                        onClick = { isAbout = true }
                    )

                    SettingsRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Logout",
                        modifier = Modifier.padding(vertical = 12.dp),
                        onClick = { isLogout = true }
                    )
                }

            }
        }
    }

    if (isAbout) {
        AlertDialog(
            onDismissRequest = { isAbout = false },
            title = { Text("Vocery") },
            text = {
                Text("Vocary - Vocab Mastery is an interactive and user-friendly vocabulary learning app designed to help learners of all levels master English words effectively")
            },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                Text(text = "OK", modifier = Modifier.clickable {
                    isAbout = false
                })
            },
            dismissButton = {

            }
        )
    }

    if (isLogout) {
        AlertDialog(
            onDismissRequest = { isLogout = false },
            title = { Text("Are you sure?") },
            text = {
                Text("You'll lose all your data")
            },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                Text(text = "Yes", modifier = Modifier.clickable {
                    scope.launch {
                        logout = true
                        isLogout = false

                        //clear all data
                        Prefs.clear()
                        onDeleteData.invoke()

                        delay(3000)

                        logout = false
                        val intent = Intent(context, AuthActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                })
            },
            dismissButton = {
                Text(text = "No", modifier = Modifier.clickable { isLogout = false })
            }
        )
    }

    if (logout) {
//        ProgressDialog {
//
//        }
    }

}


@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow Right",
        )
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    switchChecked: Boolean? = null,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        switchChecked?.let { checked ->
            onSwitchChange?.let { onChange ->
                Switch(checked = checked, onCheckedChange = onChange)
            }
        }
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
}
