package com.febriandev.vocary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.utils.Constant.LEVEL
import com.febriandev.vocary.utils.Constant.TOPIC
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.ScreenshotBox
import com.febriandev.vocary.utils.ScreenshotController
import com.febriandev.vocary.ui.components.VocabularyShare
import com.febriandev.vocary.ui.components.VocabularyTopBar
import com.febriandev.vocary.ui.content.ContentScreen
import com.febriandev.vocary.ui.items.VocabularyVerticalPager
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val vocabViewModel: VocabularyViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {

                LaunchedEffect(Unit) {
                    if (vocabViewModel.getCountNewOrNope() <= 10) startGenerateProcess(
                        "Common English Everyday Life",
                        "Intermediate"
                    )
                }

                var progressNow by remember { mutableIntStateOf(0) }
                val progressMax  = 10

                val coroutineScope = rememberCoroutineScope()

                val vocabs by vocabViewModel.vocabs.collectAsState()
                val loading by vocabViewModel.loading.collectAsState()

                var showLoadingDialog by remember { mutableStateOf(false) }

                var showContent by remember { mutableStateOf(false) }
                var showShare by remember { mutableStateOf(false) }
                var showProfile by remember { mutableStateOf(false) }

                val captureController = remember { ScreenshotController() }

                var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                var shouldCaptureScreenshot by remember { mutableStateOf(false) }

                val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                var level by remember { mutableStateOf(Prefs[LEVEL, "General Vocabulary"]) }
                var topic by remember { mutableStateOf(Prefs[TOPIC, "Beginner (A1)"]) }

                val pagerState = rememberPagerState(initialPage = 0)
                val currentPage = pagerState.currentPage

                LaunchedEffect(currentPage) {
                    val vocab = vocabs.getOrNull(currentPage) ?: return@LaunchedEffect
                    vocabViewModel.tryAddToHistory(vocab)
                }

                LaunchedEffect(shouldCaptureScreenshot) {
                    if (shouldCaptureScreenshot) {
                        showShare = true
                        captureController.capture()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { innerPadding ->

                    ScreenshotBox(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp), controller = captureController, onBitmapCaptured = {
                            capturedImage = it
                        }) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top bar: Streak & Today
                            if (!shouldCaptureScreenshot) {
                                VocabularyTopBar(
                                    name = "Febrian",
                                    streakDays = 8,
                                    todayCount = progressNow
                                )
                            }

                            VocabularyVerticalPager(
                                vocabs,
                                onShareClick = {
                                    shouldCaptureScreenshot = true
                                },
                                onProgress = {
                                    progressNow += 1
                                },
                                pagerState,
                                applicationContext,
                                vocabViewModel
                            )

                            if (!shouldCaptureScreenshot) {
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            if (!shouldCaptureScreenshot) {
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
                                        modifier = Modifier.clickable {

                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Games,
                                                contentDescription = "Info",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(24.dp)
                                            )

                                            Spacer(Modifier.width(8.dp))

                                            Text(
                                                "Play Games",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                    val isLastPage =
                                        pagerState.currentPage == (pagerState.pageCount - 1)

                                    AnimatedVisibility(visible = isLastPage) {
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            shadowElevation = 4.dp,
                                            onClick = {
//                                        showLoadingDialog = true
//                                        vocabViewModel.startGenerateProcess(
//                                            Prefs[TOPIC, "General Vocabulary"],
//                                            Prefs[LEVEL, "Beginner (A1)"]
//                                        )
//                                        coroutineScope.launch {
//                                            delay(13000) // Simulasi proses generate 10 detik
//                                            vocabViewModel.getAllDetailVocab()
//                                            delay(2000)
//                                            showLoadingDialog = false
//                                            pagerState.scrollToPage(0) // Kembali ke halaman awal
//                                        }
                                            },
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Generate New Vocab")
                                            }
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        shadowElevation = 4.dp,
                                        color = MaterialTheme.colorScheme.surface,
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


                        VocabularyShare(showShare, capturedImage, bottomSheetState) {
                            showShare = false
                            shouldCaptureScreenshot = false
                            capturedImage = null
                        }

//                        AlertDialog(
//                            containerColor = MaterialTheme.colorScheme.background,
//                            onDismissRequest = {
//                                showDialog = false
//                                shouldCaptureScreenshot = false
//                                capturedImage = null
//                            },
//                            confirmButton = {
//                                TextButton(onClick = {
//                                    shouldCaptureScreenshot = false
//                                    capturedImage?.let {
//                                        saveImageToGallery(applicationContext, it.asAndroidBitmap())
//                                    }
//                                    showDialog = false
//                                }) {
//                                    Text("Download")
//                                }
//                            },
//                            dismissButton = {
//                                TextButton(onClick = {
//                                    shouldCaptureScreenshot = false
//                                    capturedImage?.let {
//                                        shareImage(applicationContext, it.asAndroidBitmap())
//                                    }
//                                    showDialog = false
//                                }) {
//                                    Text("Share")
//                                }
//                            },
//                            text = {
//                                capturedImage?.let {
//                                    Image(
//                                        bitmap = it, contentDescription = null, modifier = Modifier
//                                            .fillMaxWidth()
//                                            .height(400.dp)
//                                    )
//                                }
//                            }
//                        )
                        // }
                    }
                    ContentScreen(showContent, applicationContext) {
                        showContent = false
                        if (level != Prefs[LEVEL, "Beginner (A1)"] || topic != Prefs[TOPIC, "General Vocabulary"]) {
                            showLoadingDialog = true
                            level = Prefs[LEVEL, "Beginner (A1)"]
                            topic = Prefs[TOPIC, "General Vocabulary"]

//                            vocabViewModel.startGenerateProcess(
//                                Prefs[TOPIC, "General Vocabulary"],
//                                Prefs[LEVEL, "Beginner (A1)"]
//                            )

                            coroutineScope.launch {
                                delay(13000) // Simulasi proses generate 10 detik
                                vocabViewModel.getAllVocabulary()
                                delay(2000)
                                showLoadingDialog = false
                                pagerState.scrollToPage(0) // Kembali ke halaman awal
                            }
                        }
                    }
//
//                    ProfileScreen(
//                        showProfile,
//                        isGreeting,
//                        isMotivation,
//                        coroutineScope,
//                        applicationContext,
//                        {
//                            //   isGreeting = it
//                        },
//                        {
//                            // isMotivation = it
//                        },
//                        {
//                            // vocabViewModel.deleteAllData()
//                        }) {
//                        showProfile = false
//                    }


                }
            }
        }
    }
}


