package com.febriandev.vocary.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.theme.VocaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SearchVocabularyActivity : ComponentActivity() {

    private val searchVocabularyViewModel: SearchVocabularyViewModel by viewModels()
 //   private val vocabViewModel: VocabViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {

                    var searchText by remember { mutableStateOf("") }

                    val isLoading = searchVocabularyViewModel.isLoading

                    LaunchedEffect(searchText) {
                        delay(500)
                        if (searchText.isNotEmpty()) {
                            searchVocabularyViewModel.fetchDefinition(searchText)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 64.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                modifier = Modifier.clickable {
                                    finish()
                                }
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Search Vocabulary",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

//                        SearchNoFilter(searchQuery = searchText) {
//                            searchText = it
//                        }
//
//                        LazyColumn(
//                            modifier = Modifier
//                        ) {
//
//                            if (isLoading) {
//                                items(4) {
//                                    ItemShimmer()
//                                }
//                            } else {
//                                if (result.isNullOrEmpty()) {
//                                    item {
//                                        Box(Modifier.fillParentMaxHeight(0.8f)) {
//                                            EmptyData()
//                                        }
//                                    }
//                                } else {
//                                    itemsIndexed(result) { i, it ->
//
//                                        val vocab = it.toVocabDetailEntity(true)
//                                        val meanings = vocab.meanings[0]
//                                        val definitions = meanings.definitions[0]
//
//                                        ItemSearchVocabulary(
//                                            word = vocab.word,
//                                            definition = "(${meanings.partOfSpeech}) ${definitions.definition})",
//                                        ) {
//                                            vocabViewModel.insertDetail(vocab)
//                                            showMessage("Success add to my own word")
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        }
    }
}