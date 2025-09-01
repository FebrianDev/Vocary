package com.febriandev.vocary.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.data.db.entity.toVocabularyEntity
import com.febriandev.vocary.ui.components.EmptyData
import com.febriandev.vocary.ui.components.SearchNoFilter
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.items.ItemSearchVocabulary
import com.febriandev.vocary.ui.shimmer.ItemShimmer
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.febriandev.vocary.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SearchVocabularyActivity : ComponentActivity() {

    private val searchVocabularyViewModel: SearchVocabularyViewModel by viewModels()
    private val vocabViewModel: VocabularyViewModel by viewModels()

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

                    val state by searchVocabularyViewModel.uiState.collectAsState()

                    LaunchedEffect(searchText) {
                        delay(500)

                        searchVocabularyViewModel.fetchDefinition(searchText)

                    }

                    LaunchedEffect(Unit) {
                        vocabViewModel.uiMessage.collect { message ->
                            applicationContext.showMessage(message)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        TitleTopBar("Search Vocabulary") {
                            finish()
                        }

                        SearchNoFilter(searchQuery = searchText) {
                            searchText = it
                        }

                        LazyColumn(
                            modifier = Modifier
                        ) {

                            if (state.isLoading) {
                                items(4) {
                                    ItemShimmer()
                                }
                            } else {
                                if (state.result.isEmpty()) {
                                    item {
                                        Box(Modifier.fillParentMaxHeight(0.8f)) {
                                            EmptyData()
                                        }
                                    }
                                } else {
                                    itemsIndexed(state.result) { i, it ->

                                        val vocab = it.toVocabularyEntity()
                                        val meanings = vocab.definitions.firstOrNull()

                                        ItemSearchVocabulary(
                                            word = vocab.word,
                                            definition = "(${meanings?.partOfSpeech}) ${meanings?.definition})",
                                        ) {
                                            vocabViewModel.insertIfNotExists(vocab)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}