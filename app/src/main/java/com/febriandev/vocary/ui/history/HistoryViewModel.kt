package com.febriandev.vocary.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.VocabularyRepository
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.SortOrder
import com.febriandev.vocary.ui.components.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val vocabRepository: VocabularyRepository) :
    ViewModel() {

    private val _sortType = MutableStateFlow(SortType.DATE)
    val sortType: StateFlow<SortType> get() = _sortType

    private val _sortOrder = MutableStateFlow(SortOrder.ASCENDING)
    val sortOrder: StateFlow<SortOrder> get() = _sortOrder

    fun updateSort(type: SortType, order: SortOrder) {
        _sortType.value = type
        _sortOrder.value = order
        applySort()
    }


    private val _vocabs = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabs: StateFlow<List<Vocabulary>> = _vocabs

    private val _loadingShimmer = MutableStateFlow(true)
    val loadingShimmer: StateFlow<Boolean> = _loadingShimmer

    private var searchJob: Job? = null
    private var loadJob: Job? = null

    fun getHistory() {
        loadJob = viewModelScope.launch {
            _loadingShimmer.value = true
            vocabRepository.getHistory().collectLatest {
                _vocabs.value = sortList(it)
                delay(300)
                _loadingShimmer.value = false
            }
        }
    }

    fun searchHistory(query: String) {
        searchJob = viewModelScope.launch {
            _loadingShimmer.value = true
            vocabRepository.searchHistory(query).collectLatest {
                _vocabs.value = sortList(it)
                delay(300)
                _loadingShimmer.value = false
            }
        }
    }

    private fun applySort() {
        _vocabs.value = sortList(_vocabs.value)
    }

    private fun sortList(list: List<Vocabulary>): List<Vocabulary> {
        return when (_sortType.value) {
            SortType.NAME -> {
                if (_sortOrder.value == SortOrder.ASCENDING) {
                    list.sortedBy { it.word.lowercase() }
                } else {
                    list.sortedByDescending { it.word.lowercase() }
                }
            }

            SortType.DATE -> {
                if (_sortOrder.value == SortOrder.ASCENDING) {
                    list.sortedByDescending { it.historyTimestamp }
                } else {
                    list.sortedBy { it.historyTimestamp }
                }
            }
        }
    }

    private val _uiMessage = Channel<String>(Channel.BUFFERED)
    val uiMessage = _uiMessage.receiveAsFlow()

    fun toggleFavorite(id: String) = viewModelScope.launch {
        Log.d("ToggleFavorite", "Checking ${id} vs $id")
        try {
            val updatedList = _vocabs.value.map { vocab ->
                if (vocab.id == id) {
                    val newFavorite = !vocab.isFavorite

                    // Simpan ke DB
                    if (newFavorite) {
                        vocabRepository.addToFavorite(id)
                        _uiMessage.send("\"${vocab.word}\" added to favorites")
                    } else {
                        vocabRepository.removeFromFavorite(id)
                        _uiMessage.send("\"${vocab.word}\" removed from favorites")
                    }

                    // Update di UI state
                    vocab.copy(isFavorite = newFavorite)
                } else vocab
            }

            _vocabs.value = updatedList
        } catch (e: Exception) {
            Log.e("ToggleFavorite", "Error: ${e.message}", e)
            _uiMessage.send("Failed to update favorite")
        }
    }

    fun addToFavorite(id: String) = viewModelScope.launch {
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                if (!vocab.isFavorite) {
                    vocabRepository.addToFavorite(id)
                    _uiMessage.send("\"${vocab.word}\" successfully added to favorites")
                    vocab.copy(isFavorite = true)
                } else {
                    _uiMessage.send("\"${vocab.word}\" is already in favorites")
                    vocab
                }
            } else vocab
        }

        _vocabs.value = updatedList
    }

    fun updateNote(id: String, note: String) = viewModelScope.launch {
        vocabRepository.updateNote(id, note)

        // Update state di memori supaya UI realtime
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                vocab.copy(note = note)
            } else vocab
        }
        _vocabs.value = updatedList

        _uiMessage.send("Note for \"${updatedList.first { it.id == id }.word}\" updated")
    }
    fun addReport(
        id: String
    ) = viewModelScope.launch {

        val reportedWord = _vocabs.value.firstOrNull { it.id == id }?.word ?: ""

        // Update DB
        vocabRepository.addReport(id)

        // Hapus dari list
        _vocabs.value = _vocabs.value.filter { it.id != id }

        // Kirim pesan
        _uiMessage.send("\"$reportedWord\" successfully reported")
    }
}