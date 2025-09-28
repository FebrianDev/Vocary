package com.febriandev.vocary.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.form.FormField
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.UserViewModel
import com.febriandev.vocary.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {

                var user by remember { mutableStateOf<UserEntity?>(null) }

                LaunchedEffect(Unit) {
                    user = userViewModel.getCurrentUser()
                }

                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .padding(24.dp),
                    ) {

                        TitleTopBar("Profile") {
                            finish()
                        }

                        LazyColumn(modifier = Modifier.weight(1f)) {
                            item {
                                FormField(user?.name ?: "", "Name") {
                                    user = user?.copy(name = it)
                                }

                                FormField(user?.email ?: "", "Email") {
                                    user = user?.copy(email = it)
                                }

                                FormField(
                                    user?.targetVocabulary?.toString() ?: "",
                                    "Target Vocabulary / Day"
                                ) {
                                    user = user?.copy(targetVocabulary = it.toIntOrNull() ?: 0)
                                }

                                FormField(
                                    user?.vocabLevel ?: "",
                                    "Vocabulary Level",
                                    readOnly = true
                                ) {
                                    user = user?.copy(vocabLevel = it)
                                }

                                FormField(
                                    user?.vocabTopic ?: "",
                                    "Vocabulary Topic",
                                    readOnly = true
                                ) {
                                    user = user?.copy(vocabTopic = it)
                                }

                                FormField(
                                    if (user?.premium == true) "Yes" else "No",
                                    "Premium",
                                    readOnly = true
                                ) {

                                }

                                FormField(
                                    user?.premiumDuration?.toString() ?: "-",
                                    "Premium Duration",
                                    readOnly = true
                                ) { }

                                FormField(
                                    user?.deviceName ?: "-",
                                    "Device Name",
                                    readOnly = true
                                ) { }

                                FormField(
                                    user?.deviceId ?: "-",
                                    "Device ID",
                                    readOnly = true
                                ) { }

                            }
                        }

                        Button(
                            onClick = {
                                when {
                                    user?.name.isNullOrBlank() -> {
                                        showMessage("Name cannot be empty")
                                    }

                                    user?.email.isNullOrBlank() -> {
                                        showMessage("Email cannot be empty")
                                    }

                                    user?.targetVocabulary == null || user?.targetVocabulary == 0 -> {
                                        showMessage("Target Vocabulary cannot be empty or 0")
                                    }

                                    else -> {
                                        user?.let { userViewModel.updateUser(it) }
                                        showMessage("Success update profile")
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .height(48.dp)

                        ) {
                            Text("Save", style = MaterialTheme.typography.labelLarge)
                        }

                    }
                }
            }
        }
    }
}