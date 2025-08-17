package com.febriandev.vocary.ui.items

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.febrian.vocery.utils.downloadAndSaveAudio
import com.febrian.vocery.utils.playAudioFromFile
import com.febrian.vocery.utils.showMessage
import com.febrian.vocery.utils.speakText
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.VocabularyCard
import com.febriandev.vocary.ui.components.VocabularyInfo
import com.febriandev.vocary.ui.components.VocabularyTranslate
import com.febriandev.vocary.ui.vm.VocabularyViewModel
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VocabularyVerticalPager(
    vocabs: List<Vocabulary>,
    shouldCaptureScreenshot: Boolean,
    onNoteCLick: (vocabulary: Vocabulary) -> Unit,
    onShareClick: (vocabulary: Vocabulary) -> Unit,
    onProgress: () -> Unit,
    pagerState: PagerState = rememberPagerState(initialPage = 0),
    applicationContext: Context = LocalContext.current,
    vocabViewModel: VocabularyViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()

    var showTranslate by remember { mutableStateOf(false) }

    var selectedVocab by remember { mutableStateOf<Vocabulary?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showInfo by remember { mutableStateOf(false) }

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

        val audioUrl =
            vocabulary.audio

        LaunchedEffect(audioUrl) {
            if (audioUrl.isEmpty()) return@LaunchedEffect
            try {
                val fileName = "${vocabulary.word}.mp3"
                downloadAndSaveAudio(applicationContext, audioUrl, fileName)
            } catch (e: Exception) {
                Log.e(
                    "AudioDownload",
                    "Failed to download audio: ${e.localizedMessage}"
                )
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
            onInfoClick = {
                selectedVocab = vocabulary
                showInfo = true
            },
            onTranslate = {
                selectedVocab = vocabulary
                showTranslate = true
            },
            onShareClick = { onShareClick.invoke(vocabulary) },
            onNotes = { onNoteCLick.invoke(vocabulary) },
            onFavoriteClick = {
                vocabViewModel.toggleFavorite(vocabulary.id)
            }) {
            if (it == SrsStatus.KNOWN) onProgress.invoke()
            vocabViewModel.updateSrsStatus(vocabulary.id, it)
        }

    }

    VocabularyInfo(showInfo, selectedVocab, bottomSheetState) {
        showInfo = false
    }

    VocabularyTranslate(showTranslate, selectedVocab, bottomSheetState) {
        showTranslate = false
    }
}