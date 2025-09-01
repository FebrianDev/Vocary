package com.febriandev.vocary.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.components.EmptyData
import com.febriandev.vocary.ui.components.SearchFilter
import com.febriandev.vocary.ui.components.SortBottomSheet
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.favorite.ListVocabularyActivity
import com.febriandev.vocary.ui.items.ItemVocabularyCard
import com.febriandev.vocary.ui.shimmer.ItemShimmer
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.OwnWordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MyOwnWordActivity : ComponentActivity() {

    private val ownWordViewModel: OwnWordViewModel by viewModels()

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

                    val sortType by ownWordViewModel.sortType.collectAsState()
                    val sortOrder by ownWordViewModel.sortOrder.collectAsState()

                    val loadingShimmer by ownWordViewModel.loadingShimmer.collectAsState()

                    LaunchedEffect(searchText) {
                        if (searchText.isBlank()) {
                            ownWordViewModel.getAllOwnWord()
                        } else {
                            delay(300)
                            ownWordViewModel.searchOwnWord(searchText)
                        }
                    }

                    val ownWord by ownWordViewModel.vocabs.collectAsState()
                    val coroutineScope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        TitleTopBar("My Own Word") {
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
                            // .padding(8.dp)
                        ) {

                            if (loadingShimmer) {
                                items(4) {
                                    ItemShimmer()
                                }
                            } else {
                                if (ownWord.isEmpty()) {
                                    item {
                                        Box(Modifier.fillParentMaxHeight(0.8f)) {
                                            EmptyData()
                                        }
                                    }
                                } else {
                                    itemsIndexed(ownWord) { i, it ->

                                        //       val vocab = it.toVocabularyEntity()
                                        val meanings = it.definitions.firstOrNull()

                                        ItemVocabularyCard(
                                            word = it.word,
                                            phonetic = it.phonetic,
                                            definition = "(${meanings?.partOfSpeech}) ${meanings?.definition}",
                                            onPlayPronunciationClick = {
//                                                coroutineScope.launch {
//                                                    val audioUrl =
//                                                        vocab.phonetics.firstOrNull { it.audio?.isNotBlank() == true }?.audio
//
//                                                    if (audioUrl.isNullOrEmpty()) {
//                                                        speakText(vocab.word)
//                                                    } else {
//                                                        val fileName = "${it.word}.mp3"
//                                                        val file = downloadAndSaveAudio(
//                                                            applicationContext,
//                                                            audioUrl,
//                                                            fileName
//                                                        )
//                                                        playAudioFromFile(file)
//                                                    }
//                                                }
                                            },
                                            timestamp = it.ownWordTimestamp ?: 0L,
                                        ) {
                                            val intent =
                                                Intent(
                                                    applicationContext,
                                                    ListVocabularyActivity::class.java
                                                )
                                            // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            intent.putExtra("position", i)
                                            intent.putExtra("type", "ownWord")
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
                                ownWordViewModel.updateSort(type, order)
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