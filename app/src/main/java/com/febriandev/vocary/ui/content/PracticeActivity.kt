package com.febriandev.vocary.ui.content

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.ui.auth.AuthActivity
import com.febriandev.vocary.ui.components.SearchFilter
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.favorite.FavoriteVocabViewModel
import com.febriandev.vocary.ui.minigame.MiniGameActivity
import com.febriandev.vocary.ui.shimmer.ItemShimmer
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.utils.DialogYesNo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PracticeActivity : ComponentActivity() {

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

                    var isTest by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {

                            TitleTopBar("Practice") {
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
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (loadingShimmer) {
                                    items(4) {
                                        ItemShimmer()
                                    }
                                } else {
                                    // isi item normal
                                }
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                isTest = true
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Add"
                            )
                        }
                    }

                    DialogYesNo(isTest, "Take a test?", "Are you sure take a test?", {
                        isTest = false
                    }) {
                        val intent = Intent(applicationContext, MiniGameActivity::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        //finish()
                    }

                }

            }
        }
    }
}