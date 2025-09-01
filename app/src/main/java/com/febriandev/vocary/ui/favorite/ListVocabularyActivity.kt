package com.febriandev.vocary.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.SortOrder
import com.febriandev.vocary.ui.components.SortType
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.components.VocabularyInfo
import com.febriandev.vocary.ui.components.VocabularyNote
import com.febriandev.vocary.ui.components.VocabularyShare
import com.febriandev.vocary.ui.history.HistoryViewModel
import com.febriandev.vocary.ui.items.VocabularyVerticalPager
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.OwnWordViewModel
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.febriandev.vocary.utils.ScreenshotBox
import com.febriandev.vocary.utils.ScreenshotController
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListVocabularyActivity : ComponentActivity() {

    private val favoriteVocabViewModel: FavoriteVocabViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val vocabularyViewModel: VocabularyViewModel by viewModels()
    private val ownWordViewModel: OwnWordViewModel by viewModels()

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

                    val type = intent.getStringExtra("type")

                    val favoriteVocab by favoriteVocabViewModel.vocabs.collectAsState()
                    val historyVocab by historyViewModel.vocabs.collectAsState()
                    val ownWordVocab by ownWordViewModel.vocabs.collectAsState()

                    val vocabs = remember(favoriteVocab, historyVocab, ownWordVocab) {
                        when (type) {
                            "favorite" -> favoriteVocab
                            "history" -> historyVocab
                            "ownWord" -> ownWordVocab
                            else -> emptyList()
                        }
                    }

                    val title = when (type) {
                        "favorite" -> "Favorite"
                        "history" -> "History"
                        "ownWord" -> "Own Word"
                        else -> ""
                    }

                    var showInfo by remember { mutableStateOf(false) }
                    var showNote by remember { mutableStateOf(false) }
                    var showShare by remember { mutableStateOf(false) }

                    var selectedVocab by remember { mutableStateOf(Vocabulary()) }

                    val pagerState = rememberPagerState(initialPage = position)

                    val captureController = remember { ScreenshotController() }
                    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                    var shouldCaptureScreenshot by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {

                        when (type) {
                            "favorite" -> {
                                if (search.isEmpty())
                                    favoriteVocabViewModel.getAllFavoriteWithDetailFlow()
                                else favoriteVocabViewModel.searchFavorite(search)

                                favoriteVocabViewModel.updateSort(sortType, sortOrder)
                            }

                            "history" -> {
                                if (search.isEmpty())
                                    historyViewModel.getHistory()
                                else historyViewModel.searchHistory(search)

                                historyViewModel.updateSort(sortType, sortOrder)
                            }

                            "ownWord" -> {
                                if (search.isEmpty())
                                    ownWordViewModel.getAllOwnWord()
                                else ownWordViewModel.searchOwnWord(search)

                                ownWordViewModel.updateSort(sortType, sortOrder)
                            }
                        }

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
                            .fillMaxSize(),

                        controller = captureController,
                        onBitmapCaptured = {
                            capturedImage = it
                        }) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize().padding(24.dp),
                        ) {

                            // Top bar: Streak & Today
                            if (!shouldCaptureScreenshot) {
                                TitleTopBar(title) {
                                    finish()
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

                                },
                                active = false,
                                pagerState,
                                applicationContext,
                                vocabularyViewModel
                            )
                        }

                        VocabularyInfo(showInfo, selectedVocab, applicationContext) {
                            showInfo = false
                        }

                        VocabularyNote(selectedVocab, showNote, vocabularyViewModel) {
                            showNote = false
                        }

                        VocabularyShare(
                            selectedVocab,
                            capturedImage,
                            applicationContext,
                            this@ListVocabularyActivity,
                            showShare,
                            vocabularyViewModel
                        ) {
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