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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.febriandev.vocary.ui.form.FormOptionTopic
import com.febriandev.vocary.ui.onboard.OnboardViewModel
import com.febriandev.vocary.ui.onboard.TopicType
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.UserViewModel
import com.febriandev.vocary.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeTopicActivity : ComponentActivity() {

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
                        val type = TopicType.entries
                            .firstOrNull { it.displayName == user?.vocabTopic }
                        if (type != null) onboardViewModel.onTopicSelected(type)
                    }

                    val selectedTopic by onboardViewModel.selectedTopic.collectAsState()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        TitleTopBar("Change Topic") {
                            finish()
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        val topics = TopicType.entries.toTypedArray()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight(0.9f)
                        ) {
                            items(topics.size) {
                                FormOptionTopic(
                                    topicType = topics[it],
                                    isSelected = topics[it] == selectedTopic
                                ) { topicType ->
                                    onboardViewModel.onTopicSelected(topicType)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                //  Prefs[TOPIC] = selectedTopic?.displayName

                                if(user == null) {
                                    showMessage("User is empty...")
                                    return@Button
                                }

                                val newUser = user?.copy(vocabTopic = selectedTopic?.name, isSync = false)
                                userViewModel.updateUser(newUser!!)
                                finish()
                            }
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