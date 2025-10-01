package com.febriandev.vocary

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.auth.AuthViewModel
import com.febriandev.vocary.ui.components.LoadingDialogContent
import com.febriandev.vocary.ui.components.VocabularyInfo
import com.febriandev.vocary.ui.components.VocabularyNote
import com.febriandev.vocary.ui.components.VocabularyShare
import com.febriandev.vocary.ui.components.VocabularyTopBar
import com.febriandev.vocary.ui.content.ContentScreen
import com.febriandev.vocary.ui.items.VocabularyVerticalPager
import com.febriandev.vocary.ui.minigame.MiniGameActivity
import com.febriandev.vocary.ui.profile.ProfileScreen
import com.febriandev.vocary.ui.progress.ProgressViewModel
import com.febriandev.vocary.ui.streaks.StreakScreen
import com.febriandev.vocary.ui.streaks.StreakViewModel
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.RevenueCatViewModel
import com.febriandev.vocary.ui.vm.UserViewModel
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.febriandev.vocary.utils.ConnHelper
import com.febriandev.vocary.utils.Constant.DAILY_GOAL
import com.febriandev.vocary.utils.Constant.LEVEL
import com.febriandev.vocary.utils.Constant.OPEN_APP
import com.febriandev.vocary.utils.Constant.TOPIC
import com.febriandev.vocary.utils.Constant.TUTORIAL
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.ScreenshotBox
import com.febriandev.vocary.utils.ScreenshotController
import com.febriandev.vocary.utils.showMessage
import com.febriandev.vocary.worker.NotificationWorker
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val vocabViewModel: VocabularyViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val progressViewModel: ProgressViewModel by viewModels()
    private val streakViewModel: StreakViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels()
    private val revenueCatViewModel: RevenueCatViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferredId = intent.getStringExtra("VOCAB_ID")

        setContent {
            VocaryTheme {

                //  val PROGRESS_MAX = 10

                LaunchedEffect(Unit) {
                    vocabViewModel.getAllVocabulary(preferredId)
                    userViewModel.getUser()
                    val user = userViewModel.getCurrentUser()
                    if (user != null) {
                        if (vocabViewModel.getCountNewOrNope() <= 15)
                            startGenerateProcess(
                                user.vocabTopic ?: "Common English",
                                user.vocabLevel ?: "Intermediate",
                                user.id
                            )

                        val constraints = Constraints.Builder()
                            .setRequiresBatteryNotLow(true)
                            .setRequiresDeviceIdle(false)
                            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                            .build()

                        val request =
                            PeriodicWorkRequestBuilder<NotificationWorker>(4, TimeUnit.HOURS)
                                .setConstraints(constraints)
                                .setInitialDelay(4, TimeUnit.HOURS)
                                .build()

                        WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                            "daily_notification",
                            ExistingPeriodicWorkPolicy.UPDATE,
                            request
                        )
                    }

                    progressViewModel.getProgress()
                    streakViewModel.markStreak(OPEN_APP, true)

                }

                val dailyProgress by progressViewModel.dailyProgress.collectAsState()
                val user by userViewModel.user.collectAsState()

                val coroutineScope = rememberCoroutineScope()

                val vocabs by vocabViewModel.vocabs.collectAsState()
                //val loading by vocabViewModel.loading.collectAsState()

                var showLoadingDialog by remember { mutableStateOf(false) }

                var showContent by remember { mutableStateOf(false) }
                var showInfo by remember { mutableStateOf(false) }
                var showNote by remember { mutableStateOf(false) }
                var showShare by remember { mutableStateOf(false) }
                var showProfile by remember { mutableStateOf(false) }

                var selectedVocab by remember { mutableStateOf(Vocabulary()) }

                val captureController = remember { ScreenshotController() }

                var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                var shouldCaptureScreenshot by remember { mutableStateOf(false) }

                var level by remember { mutableStateOf(Prefs[LEVEL, "General Vocabulary"]) }
                var topic by remember { mutableStateOf(Prefs[TOPIC, "Beginner (A1)"]) }

                val pagerState = rememberPagerState(initialPage = 0)
                val currentPage = pagerState.currentPage

                LaunchedEffect(currentPage, vocabs) {
                    val vocab = vocabs.getOrNull(currentPage) ?: return@LaunchedEffect
                    vocabViewModel.tryAddToHistory(vocab)
                }

                LaunchedEffect(shouldCaptureScreenshot) {
                    if (shouldCaptureScreenshot) {
                        showShare = true
                        captureController.capture()
                    }
                }

                LaunchedEffect(user?.id) {
                    level = user?.vocabLevel ?: "Intermediate"
                    topic = user?.vocabTopic ?: "English Everyday Life"

                    if (user != null && user?.revenueCat == true) {
                        userViewModel.syncPremiumStatus(user!!)
                    }
                }

                var showTutorial by remember { mutableStateOf(Prefs[TUTORIAL, false]) }

                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { innerPadding ->

                    ScreenshotBox(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(), controller = captureController, onBitmapCaptured = {
                            capturedImage = it
                        }) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Top bar: Streak & Today
                            if (!shouldCaptureScreenshot) {
                                if (user != null) {
                                    VocabularyTopBar(
                                        isPremium = user?.premium ?: false,
                                        name = user?.name ?: "",
                                        streakDays = 8,
                                        todayCount = dailyProgress?.progress ?: 0,
                                        dailyGoal = user?.targetVocabulary ?: 100,
                                        xp = user?.xp ?: 0
                                    )
                                }
                            }

                            VocabularyVerticalPager(
                                vocabs,
                                shouldCaptureScreenshot,
                                onInfoCLick = {
                                    selectedVocab = it
                                    showInfo = true
                                },
                                onNoteCLick = {
                                    selectedVocab = it
                                    showNote = true
                                },
                                onShareClick = {
                                    selectedVocab = it
                                    shouldCaptureScreenshot = true
                                },

                                onProgress = { id ->
                                    progressViewModel.onVocabularyKnown(
                                        id,
                                        user?.targetVocabulary ?: 0,
                                        user?.id ?: "",
                                        user?.xp ?: 0
                                    ) {
                                        userViewModel.getUser()
                                    }
                                    if (dailyProgress != null && dailyProgress?.progress!! >= (user?.targetVocabulary
                                            ?: 0) - 1
                                    ) {
                                        streakViewModel.markStreak(DAILY_GOAL, true)
                                    }

                                },
                                active = true,
                                pagerState,
                                applicationContext,
                                vocabViewModel,
                                ""
                            )

                            if (!shouldCaptureScreenshot) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (!shouldCaptureScreenshot) {

                                val isLastPage =
                                    pagerState.currentPage == (pagerState.pageCount - 1)

                                AnimatedVisibility(visible = isLastPage) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        shadowElevation = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        tonalElevation = 4.dp,
                                        onClick = {
                                            showLoadingDialog = true
                                            coroutineScope.launch {
                                                val user = userViewModel.getCurrentUser()
                                                if (user != null) {
                                                    startGenerateProcess(
                                                        user.vocabTopic ?: "Common English",
                                                        user.vocabLevel ?: "Intermediate",
                                                        user.id
                                                    )
                                                    delay(13000)
                                                    vocabViewModel.getAllVocabulary()
                                                    delay(2000)
                                                    showLoadingDialog = false
                                                    pagerState.scrollToPage(0)
                                                }
                                            }
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text("Generate New Vocab")
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        shadowElevation = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        tonalElevation = 4.dp,
                                        modifier = Modifier.clickable {
                                            showContent = true
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.GridView,
                                            contentDescription = "Info",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .size(24.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        shadowElevation = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        tonalElevation = 4.dp,
                                        modifier = Modifier.clickable {

                                            if (user?.premium == false) {
                                                showMessage("You need to access premium!")
                                                return@clickable
                                            }

                                            val intent = Intent(
                                                applicationContext,
                                                MiniGameActivity::class.java
                                            )
                                            startActivity(intent)
                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Icon(
                                                Lucide.GraduationCap,
                                                contentDescription = "Info",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(24.dp)
                                            )

                                            Spacer(Modifier.width(8.dp))

                                            Text(
                                                "Practice",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        shadowElevation = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        tonalElevation = 4.dp,
                                        modifier = Modifier.clickable {
                                            showProfile = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Person",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        VocabularyInfo(showInfo, selectedVocab, applicationContext) {
                            showInfo = false
                        }

                        VocabularyNote(selectedVocab, showNote, type = "", vocabViewModel) {
                            showNote = false
                        }

                        VocabularyShare(
                            selectedVocab,
                            capturedImage,
                            applicationContext,
                            this@MainActivity,
                            showShare,
                            vocabViewModel,
                            type = ""
                        ) {
                            showShare = false
                            shouldCaptureScreenshot = false
                            capturedImage = null
                        }

                        StreakScreen()
                    }

                    if (showTutorial) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xAA000000))
                                .clickable {
                                    showTutorial = false
                                    Prefs[TUTORIAL] = false
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Swipe up or down to explore words",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                val infiniteTransition = rememberInfiniteTransition()
                                val offsetY by infiniteTransition.animateFloat(
                                    initialValue = 0f,
                                    targetValue = 30f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(800, easing = LinearEasing),
                                        repeatMode = RepeatMode.Reverse
                                    )
                                )

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Swipe Up Arrow",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .offset(y = -offsetY.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Swipe Down Arrow",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .offset(y = offsetY.dp)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    "Tap icons to play pronunciation, info, notes, share, or favorite",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                Button(onClick = {
                                    showTutorial = false
                                    Prefs[TUTORIAL] = false
                                }) {
                                    Text("Got it!")
                                }
                            }
                        }
                    }
                }

                ContentScreen(user?.premium ?: false, showContent, applicationContext) {
                    showContent = false
                    userViewModel.getUser()
//                    if (level != user?.vocabLevel || topic != user?.vocabTopic) {
//                        //  showLoadingDialog = true
//                        level = user?.vocabLevel ?: "Intermediate"
//                        topic = user?.vocabTopic ?: "Every day life"

//                            vocabViewModel.startGenerateProcess(
//                                Prefs[TOPIC, "General Vocabulary"],
//                                Prefs[LEVEL, "Beginner (A1)"]
//                            )

                    coroutineScope.launch {

                        val user = userViewModel.getCurrentUser()
                        if (user != null && level != user.vocabLevel || topic != user?.vocabTopic) {
                            showLoadingDialog = true
                            level = user?.vocabLevel ?: "Intermediate"
                            topic = user?.vocabTopic ?: "Common English"
                            startGenerateProcess(
                                user?.vocabTopic ?: "Common English",
                                user?.vocabLevel ?: "Intermediate",
                                user?.id.toString()
                            )

                            delay(13000) // Simulasi proses generate 10 detik
                            vocabViewModel.getAllVocabulary()
                            delay(2000)
                            showLoadingDialog = false
                            pagerState.scrollToPage(0) // Kembali ke halaman awal
                        }
                    }
                    //    }
                }
//
                ProfileScreen(
                    user,
                    showProfile,
                    coroutineScope,
                    applicationContext,
                    this@MainActivity,
                    {
                        if (ConnHelper.hasConnection(applicationContext)) {
                            syncData(user?.id.toString())
                        } else {
                            showMessage("No Internet Connection!")
                        }

                    },
                    {

                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()

                        vocabViewModel.deleteAllData()
                        authViewModel.signOut(gso, applicationContext)
                        revenueCatViewModel.logOut()
                        OneSignal.logout()
                    }) {
                    userViewModel.getUser()
                    showProfile = false
                }


                if (showLoadingDialog) {
                    AlertDialog(
                        onDismissRequest = {},
                        confirmButton = {},
                        title = { Text("Generating Vocabulary...") },
                        text = {
                            LoadingDialogContent()
                        },
                        containerColor = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }

    @Composable
    fun FlyingButton() {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp

        val infiniteTransition = rememberInfiniteTransition()

        // animasi Y (naik turun)
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,                     // mulai dari bawah
            targetValue = -200f,                   // naik sejauh 200px
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { /* action */ },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .graphicsLayer {
                        translationY = offsetY
                    }
            ) {
                Text("Fly")
            }
        }
    }

}


