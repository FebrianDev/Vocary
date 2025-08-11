package com.febriandev.vocary.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.components.SortOrder
import com.febriandev.vocary.ui.components.SortType
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.components.VocabularyShare
import com.febriandev.vocary.ui.items.VocabularyVerticalPager
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.febriandev.vocary.utils.ScreenshotBox
import com.febriandev.vocary.utils.ScreenshotController
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListHistoryActivity : ComponentActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private val vocabularyViewModel: VocabularyViewModel by viewModels()
//    private val translationViewModel: TranslationViewModel by viewModels()
//    private val helper by lazy {
//        TranslationHelper(application.applicationContext)
//    }

    @OptIn(ExperimentalMaterial3Api::class)
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

                    val position = intent.getIntExtra("position", 0)
                    val search = intent.getStringExtra("search") ?: ""
                    val sortType = runCatching {
                        SortType.valueOf(intent.getStringExtra("sortType") ?: "")
                    }.getOrDefault(SortType.DATE)

                    val sortOrder = runCatching {
                        SortOrder.valueOf(intent.getStringExtra("sortOrder") ?: "")
                    }.getOrDefault(
                        SortOrder.ASCENDING
                    )

                    val vocabs by historyViewModel.vocabs.collectAsState()

                    val bottomSheetState =
                        rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    var showShare by remember { mutableStateOf(false) }

                    val pagerState = rememberPagerState(initialPage = position)

                    val captureController = remember { ScreenshotController() }
                    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                    var shouldCaptureScreenshot by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {

                        //  translationViewModel.downloadModel()

                        if (search.isEmpty())
                            historyViewModel.getHistory()
                        else historyViewModel.searchHistory(search)

                        historyViewModel.updateSort(sortType, sortOrder)
                    }

                    LaunchedEffect(shouldCaptureScreenshot) {
                        if (shouldCaptureScreenshot) {
                            showShare = true
                            captureController.capture()
                        }
                    }

                    ScreenshotBox(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp),
                        controller = captureController,
                        onBitmapCaptured = {
                            capturedImage = it
                        }) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {

                            if (!shouldCaptureScreenshot) {
                                TitleTopBar("History") {
                                    finish()
                                }
                            }

                            VocabularyVerticalPager(
                                vocabs,
                                onShareClick = {
                                    shouldCaptureScreenshot = true
                                },
                                onProgress = {},
                                pagerState,
                                applicationContext,
                                vocabularyViewModel
                            )
                        }

                        VocabularyShare(showShare, capturedImage, bottomSheetState) {
                            showShare = false
                            shouldCaptureScreenshot = false
                            capturedImage = null
                        }
                    }
                }
            }
        }
    }
}