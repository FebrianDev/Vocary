package com.febriandev.vocary.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.VocabularyRepository
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.SortOrder
import com.febriandev.vocary.ui.components.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnWordViewModel @Inject constructor(private val repository: VocabularyRepository) :
    ViewModel() {

    private val _vocabs = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabs: StateFlow<List<Vocabulary>> = _vocabs

    private val _sortType = MutableStateFlow(SortType.DATE)
    val sortType: StateFlow<SortType> get() = _sortType

    private val _sortOrder = MutableStateFlow(SortOrder.ASCENDING)
    val sortOrder: StateFlow<SortOrder> get() = _sortOrder

    fun updateSort(type: SortType, order: SortOrder) {
        _sortType.value = type
        _sortOrder.value = order
        applySort()
    }

    private var searchJob: Job? = null
    private var loadJob: Job? = null

    private val _loadingShimmer = MutableStateFlow(true)
    val loadingShimmer: StateFlow<Boolean> = _loadingShimmer

    fun getAllOwnWord() {
        loadJob = viewModelScope.launch {
            _loadingShimmer.value = true
            _vocabs.value = sortList(repository.getAllOwnWord())
            delay(300)
            _loadingShimmer.value = false
        }
    }

    fun searchOwnWord(query: String) {
        searchJob = viewModelScope.launch {
            _loadingShimmer.value = true
            _vocabs.value = sortList(repository.searchOwnWord(query))
            delay(300)
            _loadingShimmer.value = false
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
                    list.sortedByDescending { it.ownWordTimestamp }
                } else {
                    list.sortedBy { it.ownWordTimestamp }
                }
            }
        }
    }

}