package com.febriandev.vocary.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.components.EmptyData
import com.febriandev.vocary.ui.components.SearchFilter
import com.febriandev.vocary.ui.components.SortBottomSheet
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.items.ItemVocabularyCard
import com.febriandev.vocary.ui.shimmer.ItemShimmer
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.utils.downloadAndSaveAudio
import com.febriandev.vocary.utils.playAudioFromFile
import com.febriandev.vocary.utils.speakText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoriteActivity : ComponentActivity() {

    private val vocabFavoriteViewModel: FavoriteVocabViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
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

                    var searchText by remember { mutableStateOf("") }
                    var showSortSheet by remember { mutableStateOf(false) }
                    val sortType by vocabFavoriteViewModel.sortType.collectAsState()
                    val sortOrder by vocabFavoriteViewModel.sortOrder.collectAsState()

                    val loadingShimmer by vocabFavoriteViewModel.loadingShimmer.collectAsState()

                    LaunchedEffect(searchText) {
                        if (searchText.isBlank()) {
                            vocabFavoriteViewModel.getAllFavoriteWithDetailFlow()
                        } else {
                            delay(300)
                            vocabFavoriteViewModel.searchFavorite(searchText)
                        }
                    }

                    val favorite by vocabFavoriteViewModel.vocabs.collectAsState()
                    val coroutineScope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {

                        TitleTopBar("Favorite") {
                            finish()
                        }

                        SearchFilter(
                            searchQuery = searchText,
                            onSearchChange = {
                                searchText = it
                            },
                        ) {
                            showSortSheet = true
                        }

                        LazyColumn(
                            modifier = Modifier
                        ) {

                            if (loadingShimmer) {
                                items(4) {
                                    ItemShimmer()
                                }
                            } else {
                                if (favorite.isEmpty()) {
                                    item {
                                        Box(Modifier.fillParentMaxHeight(0.8f)) {
                                            EmptyData()
                                        }
                                    }
                                } else {
                                    itemsIndexed(favorite) { i, vocabulary ->

                                        val audioUrl =
                                            vocabulary.audio

                                        LaunchedEffect(audioUrl) {
                                            if (audioUrl.isEmpty()) return@LaunchedEffect
                                            try {
                                                val fileName = "${vocabulary.word}.mp3"
                                                downloadAndSaveAudio(
                                                    applicationContext,
                                                    audioUrl,
                                                    fileName
                                                )
                                            } catch (e: Exception) {
                                                Log.e(
                                                    "AudioDownload",
                                                    "Failed to download audio: ${e.localizedMessage}"
                                                )
                                            }
                                        }

                                        ItemVocabularyCard(
                                            word = vocabulary.word,
                                            phonetic = vocabulary.phonetic,
                                            definition = vocabulary.definition,
                                            onPlayPronunciationClick = {
                                                coroutineScope.launch {
                                                    try {
                                                        if (audioUrl.isEmpty()) {
                                                            speakText(vocabulary.word)
                                                        } else {
                                                            val fileName = "${vocabulary.word}.mp3"
                                                            val file = downloadAndSaveAudio(
                                                                applicationContext,
                                                                audioUrl,
                                                                fileName
                                                            )
                                                            playAudioFromFile(file)
                                                        }
                                                    } catch (e: Exception) {
                                                        Log.e(
                                                            "PlayAudio",
                                                            "Error while playing pronunciation: ${e.localizedMessage}"
                                                        )
                                                        speakText(vocabulary.word) // fallback ke TTS Android
                                                    }

                                                }
                                            },
                                            timestamp = vocabulary.favoriteTimestamp ?: 0L,
                                        ) {
                                            val intent =
                                                Intent(
                                                    applicationContext,
                                                    ListVocabularyActivity::class.java
                                                )
                                            // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            intent.putExtra("position", i)
                                            intent.putExtra("type", "favorite")
                                            intent.putExtra("search", searchText)
                                            intent.putExtra("sortType", sortType.name)
                                            intent.putExtra("sortOrder", sortOrder.name)
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (showSortSheet) {
                        SortBottomSheet(
                            sortType = sortType,
                            sortOrder = sortOrder,
                            onSortChange = { type, order ->
                                vocabFavoriteViewModel.updateSort(type, order)
                                showSortSheet = false
                            },
                            onDismiss = { showSortSheet = false }
                        )
                    }

                }
            }
        }
    }
}