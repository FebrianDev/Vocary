package com.febriandev.vocary.ui.items

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.VocabularyCard
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.febriandev.vocary.utils.ConnHelper
import com.febriandev.vocary.utils.Constant.PRONOUNCE
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.downloadAndSaveAudio
import com.febriandev.vocary.utils.playAudioFromFile
import com.febriandev.vocary.utils.showMessage
import com.febriandev.vocary.utils.speakText
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VocabularyVerticalPager(
    vocabs: List<Vocabulary>,
    shouldCaptureScreenshot: Boolean,
    onInfoCLick: (vocabulary: Vocabulary) -> Unit = {},
    onNoteCLick: (vocabulary: Vocabulary) -> Unit,
    onShareClick: (vocabulary: Vocabulary) -> Unit,
    onProgress: (id: String) -> Unit = {},
    active: Boolean = true,
    pagerState: PagerState = rememberPagerState(initialPage = 0),
    applicationContext: Context = LocalContext.current,
    vocabViewModel: VocabularyViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()

    var lastHandledPage by remember { mutableIntStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vocabViewModel.uiMessage.collect { message ->
            applicationContext.showMessage(message)
        }
    }

    VerticalPager(
        count = vocabs.size,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(
                0.8f
            ),
        state = pagerState
    ) { page ->

        val vocabulary = vocabs[page]

        val audioUrl = remember(vocabulary) {
            vocabulary.audio
        }

        LaunchedEffect(pagerState.currentPage) {

            if (!Prefs[PRONOUNCE, false]) return@LaunchedEffect

            val page = pagerState.currentPage
            if (page != lastHandledPage) {
                lastHandledPage = page
                val vocabulary = vocabs[page]

                // Jika ada audio URL, download di IO thread
                val audioUrl = vocabulary.audio
                var audioFile: File? = null
                if (audioUrl.isNotEmpty()) {
                    try {
                        withContext(Dispatchers.IO) {
                            val fileName = "${vocabulary.word}.mp3"
                            audioFile = downloadAndSaveAudio(applicationContext, audioUrl, fileName)
                        }
                    } catch (e: Exception) {
                        Log.e("AudioDownload", "Failed: ${e.localizedMessage}")
                    }
                }

                // Play audio jika tersedia, jika tidak atau error pakai TTS
                coroutineScope.launch(Dispatchers.Default) {
                    try {
                        if (ConnHelper.hasConnection(applicationContext)) {
                            speakText(vocabulary.word)
                        } else {
                            if (audioFile != null) {
                                playAudioFromFile(audioFile!!)
                            } else {
                                withContext(Dispatchers.Main) {
                                    applicationContext.showMessage("Failed download audio!")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("AudioPlay", "Error: ${e.localizedMessage}")
                        // fallback ke TTS
                        withContext(Dispatchers.Main) {
                            applicationContext.showMessage("Failed download audio!")
                        }
                    }
                }
            }
        }

        VocabularyCard(
            word = vocabulary.word,
            phonetic = vocabulary.phonetic,
            partOfSpeech = vocabulary.partOfSpeech,
            definition = vocabulary.definition,
            example = vocabulary.examples.firstOrNull { it.isNotEmpty() }
                ?: "",
            isFavorite = vocabulary.isFavorite,
            srsStatus = vocabulary.srsStatus,
            shouldCaptureScreenshot = shouldCaptureScreenshot,
            active = active,
            onPlayPronunciationClick = {
                if (isPlaying) return@VocabularyCard // abaikan klik saat sedang play
                isPlaying = true
                coroutineScope.launch {
                    try {
                        val file: File? = if (audioUrl.isNotEmpty()) {
                            val fileName = "${vocabulary.word}.mp3"
                            val cachedFile = File(applicationContext.cacheDir, fileName)
                            if (cachedFile.exists()) cachedFile
                            else downloadAndSaveAudio(applicationContext, audioUrl, fileName)
                        } else null

                        if (ConnHelper.hasConnection(applicationContext)) {
                            speakText(vocabulary.word)
                        } else {
                            if (file != null) {
                                playAudioFromFile(file)
                            } else {
                                withContext(Dispatchers.Main) {
                                    applicationContext.showMessage("Failed download audio!")
                                }
                            }
                        }
                        delay(500)
                    } catch (e: Exception) {
                        Log.e("PlayAudio", "Error: ${e.localizedMessage}")
                        withContext(Dispatchers.Main) {
                            applicationContext.showMessage("Failed download audio!")
                        }
                        delay(500)
                    } finally {
                        isPlaying = false
                    }
                }
            },
            onInfoClick = {
                onInfoCLick.invoke(vocabulary)
            },
            onShareClick = { onShareClick.invoke(vocabulary) },
            onNotes = { onNoteCLick.invoke(vocabulary) },
            onFavoriteClick = {
                vocabViewModel.toggleFavorite(vocabulary.id)
            }) {
            if (it == SrsStatus.KNOWN) onProgress.invoke(vocabulary.id)
            vocabViewModel.updateSrsStatus(vocabulary.id, it)
        }

    }
}