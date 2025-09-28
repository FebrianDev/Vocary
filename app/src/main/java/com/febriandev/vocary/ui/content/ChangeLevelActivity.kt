package com.febriandev.vocary.ui.content

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.form.FormOptionField
import com.febriandev.vocary.ui.onboard.LevelType
import com.febriandev.vocary.ui.onboard.OnboardViewModel
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLevelActivity : ComponentActivity() {

    private val onboardViewModel: OnboardViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {
                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { innerPadding ->

                    val user by userViewModel.user.collectAsState()

                    LaunchedEffect(Unit) {
                        userViewModel.getUser()
                    }

                    LaunchedEffect(user) {
                        onboardViewModel.level.value = user?.vocabLevel ?: ""
                    }

                    val level by onboardViewModel.level.collectAsState()
                    val levels = LevelType.entries.toTypedArray()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {

                        TitleTopBar("Change Level") {
                            finish()
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(modifier = Modifier) {
                            levels.forEach { data ->
                                FormOptionField(
                                    option = data.displayName,
                                    isSelected = data.displayName == level
                                ) {
                                    onboardViewModel.level.value = it
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                //Prefs[LEVEL] = level
                                val newUser = user?.copy(vocabLevel = level)
                                userViewModel.updateUser(newUser!!)
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Save",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                }
            }
        }

    }
}