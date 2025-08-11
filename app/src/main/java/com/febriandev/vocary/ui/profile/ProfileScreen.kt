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
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    showProfile: Boolean,
    isGreeting: Boolean,
    isMotivation: Boolean,
    scope: CoroutineScope,
    context: Context,
    onGreeting: (greeting: Boolean) -> Unit,
    onMotivation: (motivation: Boolean) -> Unit,
    onDeleteData: () -> Unit,
    onDismiss: () -> Unit
) {

//    var isCheckedNotification by remember { mutableStateOf(Prefs[NOTIFICATION, true]) }
//
//    var isAbout by remember { mutableStateOf(false) }
//    var isLogout by remember { mutableStateOf(false) }
//    var logout by remember {
//        mutableStateOf(false)
//    }
//
//    CustomAnimatedModalSheet(show = showProfile, onDismiss = onDismiss) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Text(text = "Setting", style = MaterialTheme.typography.headlineMedium)
//            Spacer(Modifier.height(16.dp))
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(2.dp),
//                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//            ) {
//
//                Column {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//
//                        Row {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = "Greeting",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Greeting",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//
//                        Switch(
//                            checked = isGreeting,
//                            onCheckedChange = {
//                                Prefs[GREETING] = it
//                                onGreeting(it)
//                            }
//                        )
//                    }
//
//                    Divider()
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//
//                        Row {
//                            Icon(
//                                imageVector = Icons.Default.DriveFileRenameOutline,
//                                contentDescription = "Motivation",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Motivation",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//
//                        Switch(
//                            checked = isMotivation,
//                            onCheckedChange = {
//                                Prefs[MOTIVATION] = it
//                                onMotivation(it)
//                            }
//                        )
//                    }
//
//                    Divider()
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Row {
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = "Notification",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Notification",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//
//                        Switch(
//                            checked = isCheckedNotification,
//                            onCheckedChange = {
//                                isCheckedNotification = it
//                                Prefs[NOTIFICATION] = it
//                            }
//                        )
//
//                    }
//
//                    Divider()
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 16.dp)
//                            .clickable {
//                                isAbout = true
//                            },
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Info,
//                            contentDescription = "Info",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "About",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//
//                    }
//
//                    Divider()
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 16.dp)
//                            .clickable {
//                                isLogout = true
//                            },
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Logout,
//                            contentDescription = "Logout",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "Logout",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//
//                    }
//                }
//            }
//        }
//    }
//
//    if (isAbout) {
//        AlertDialog(
//            onDismissRequest = { isAbout = false },
//            title = { Text("Vocery") },
//            text = {
//                Text("Vocery - Vocab Mastery is an interactive and user-friendly vocabulary learning app designed to help learners of all levels master English words effectively")
//            },
//            containerColor = baseScreenColor,
//            confirmButton = {
//                Text(text = "OK", modifier = Modifier.clickable {
//                    isAbout = false
//                })
//            },
//            dismissButton = {
//
//            }
//        )
//    }
//
//    if (isLogout) {
//        AlertDialog(
//            onDismissRequest = { isLogout = false },
//            title = { Text("Are you sure?") },
//            text = {
//                Text("You'll lose all your data")
//            },
//            containerColor = baseScreenColor,
//            confirmButton = {
//                Text(text = "Yes", modifier = Modifier.clickable {
//                    scope.launch {
//                        logout = true
//                        isLogout = false
//
//                        //clear all data
//                        Prefs.clear()
//                        onDeleteData.invoke()
//
//                        delay(3000)
//
//                        logout = false
//                        val intent = Intent(context, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        context.startActivity(intent)
//                        (context as Activity).finish()
//                    }
//                })
//            },
//            dismissButton = {
//                Text(text = "No", modifier = Modifier.clickable { isLogout = false })
//            }
//        )
//    }
//
//    if (logout) {
//        ProgressDialog {
//
//        }
//    }

}