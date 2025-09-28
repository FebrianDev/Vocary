package com.febriandev.vocary.ui.onboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.BaseActivity
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.domain.AppUser
import com.febriandev.vocary.ui.SubscriptionActivity
import com.febriandev.vocary.ui.form.FormField
import com.febriandev.vocary.ui.form.FormOptionField
import com.febriandev.vocary.ui.form.FormOptionTopic
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.UserViewModel
import com.febriandev.vocary.utils.Constant.GENDER
import com.febriandev.vocary.utils.Constant.GOAL
import com.febriandev.vocary.utils.Constant.LEVEL
import com.febriandev.vocary.utils.Constant.NAME
import com.febriandev.vocary.utils.Constant.OLD
import com.febriandev.vocary.utils.Constant.STEP_SCREEN
import com.febriandev.vocary.utils.Constant.TOPIC
import com.febriandev.vocary.utils.Constant.WORD
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.generateRandomId
import com.febriandev.vocary.utils.getAppId
import com.febriandev.vocary.utils.getDeviceName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardActivity : BaseActivity() {

    private val onboardViewModel: OnboardViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Prefs[STEP_SCREEN] = 1
        val user: AppUser? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", AppUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("user")
        }

        if (user?.name != null) onboardViewModel.name.value = user.name

        setContent {
            VocaryTheme {

                val formStep by onboardViewModel.formStep.collectAsState()

                val name by onboardViewModel.name.collectAsState()
                val old by onboardViewModel.old.collectAsState()
                val gender by onboardViewModel.gender.collectAsState()
                val word by onboardViewModel.word.collectAsState()
                val level by onboardViewModel.level.collectAsState()
                val goal by onboardViewModel.goal.collectAsState()
                val selectedTopic by onboardViewModel.selectedTopic.collectAsState()

                BackHandler(enabled = formStep != FormStep.NAME) {
                    onboardViewModel.prevStep()
                }


                AnimatedContent(
                    targetState = formStep,
                    transitionSpec = {
                        fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                    },
                    label = "Form Step Animation"
                ) { step ->

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
                            verticalArrangement = Arrangement.Center
                        ) {

                            when (step) {

                                FormStep.NAME -> {
                                    Text(
                                        text = "What do you want to be called?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    FormField(name, "Enter your name") {
                                        onboardViewModel.name.value = it
                                    }
                                }

                                FormStep.OLD -> {
                                    val olds = listOf(
                                        "13 to 17",
                                        "18 to 24",
                                        "25 to 34",
                                        "36 to 44",
                                        "45 to 54",
                                        "55+"
                                    )
                                    Text(
                                        text = "How old are you?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    olds.forEach { data ->
                                        FormOptionField(option = data, isSelected = data == old) {
                                            onboardViewModel.old.value = it
                                        }
                                    }
                                }

                                FormStep.GENDER -> {
                                    val genders = listOf("Male", "Female", "Prefer not to say")
                                    Text(
                                        text = "What's your gender?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    genders.forEach { data ->
                                        FormOptionField(
                                            option = data,
                                            isSelected = data == gender
                                        ) {
                                            onboardViewModel.gender.value = it
                                        }
                                    }
                                }

                                FormStep.WORD -> {
                                    val words = listOf(
                                        "10",
                                        "30",
                                        "50",
                                        "100"
                                    )
                                    Text(
                                        text = "How many words do you want to learn in a week?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    words.forEach { data ->
                                        FormOptionField(
                                            option = data,
                                            targetVocabulary = true,
                                            isSelected = data == word
                                        ) {
                                            onboardViewModel.word.value = it
                                        }
                                    }
                                }

                                FormStep.LEVEL -> {
                                    val levels = LevelType.entries.toTypedArray()
                                    Text(
                                        text = "What's your vocabulary level?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    levels.forEach { data ->
                                        FormOptionField(
                                            option = data.displayName,
                                            isSelected = data.displayName == level
                                        ) {
                                            onboardViewModel.level.value = it
                                        }
                                    }
                                }

                                FormStep.GOAL -> {
                                    val goals = listOf(
                                        "Improve my job prospects",
                                        "Get ready for a test",
                                        "Enhance my vocabulary",
                                        "Learn for fun",
                                        "Become bilingual",
                                        "Other"
                                    )
                                    Text(
                                        text = "What's your specific goal?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    goals.forEach { data ->
                                        FormOptionField(option = data, isSelected = data == goal) {
                                            onboardViewModel.goal.value = it
                                        }
                                    }
                                }

                                FormStep.TOPIC -> {
                                    Text(
                                        text = "What topic do you want to learn?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    val topics = TopicType.entries.toTypedArray()
                                    LazyColumn(modifier = Modifier.fillMaxHeight(0.7f)) {
                                        items(topics.size) {
                                            FormOptionTopic(
                                                topicType = topics[it],
                                                isSelected = topics[it] == selectedTopic
                                            ) { topicType ->
                                                onboardViewModel.onTopicSelected(topicType)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                if (formStep != FormStep.NAME) {
                                    Button(
                                        onClick = { onboardViewModel.prevStep() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                    ) {
                                        Text(
                                            "Previous",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                Button(
                                    onClick = {
                                        if (formStep != FormStep.TOPIC) onboardViewModel.nextStep()
                                        else {
                                            Prefs[NAME] = name
                                            Prefs[OLD] = old
                                            Prefs[GENDER] = gender
                                            Prefs[WORD] = word
                                            Prefs[LEVEL] = level
                                            Prefs[GOAL] = goal
                                            Prefs[TOPIC] = selectedTopic?.displayName

                                            val userEntity = UserEntity(
                                                id = user?.uid ?: generateRandomId(),
                                                name = name,
                                                email = user?.email ?: "",
                                                photoUrl = "",
                                                age = old.toIntOrNull(),
                                                targetVocabulary = word.toIntOrNull() ?: 0,
                                                learningGoal = goal,
                                                vocabLevel = level,
                                                vocabTopic = selectedTopic?.displayName,
                                                premium = false, // default, nanti bisa diupdate dari Firestore/RevenueCat
                                                premiumDuration = null,
                                                deviceName = getDeviceName(),
                                                deviceId = getAppId(applicationContext)
                                            )

                                            // userViewModel.saveUser(userEntity)

                                            val intent =
                                                Intent(
                                                    applicationContext,
                                                    SubscriptionActivity::class.java
                                                )

                                            intent.putExtra("user", userEntity)
                                            intent.putExtra("level", level)
                                            intent.putExtra("topic", selectedTopic?.displayName)

                                            startActivity(intent)
                                            finish()

                                        }
                                    },
                                    enabled = when (formStep) {
                                        FormStep.NAME -> name.isNotBlank()
                                        FormStep.OLD -> old.isNotBlank()
                                        FormStep.GENDER -> gender.isNotBlank()
                                        FormStep.WORD -> word.isNotBlank()
                                        FormStep.LEVEL -> level.isNotBlank()
                                        FormStep.GOAL -> goal.isNotBlank()
                                        FormStep.TOPIC -> selectedTopic != null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Text(
                                        if (formStep == FormStep.TOPIC) "Start Learning" else "Next",
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
