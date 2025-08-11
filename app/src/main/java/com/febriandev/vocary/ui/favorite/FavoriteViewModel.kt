package com.febriandev.vocary.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.FavoriteRepository
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.components.SortOrder
import com.febriandev.vocary.ui.components.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteVocabViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    private val _favoriteMessage = MutableSharedFlow<String>()
    val favoriteMessage: SharedFlow<String> = _favoriteMessage

    private val _loadingShimmer = MutableStateFlow(true)
    val loadingShimmer: StateFlow<Boolean> = _loadingShimmer

    fun toggleFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = repository.isFavorite(id)
            Log.d("isFavorite2", isFav.toString() + " " + id)
            if (isFav) {
                repository.removeFromFavorite(id)
                _favoriteMessage.emit("Removed from favorites")
            } else {
                repository.addToFavorite(id)
                _favoriteMessage.emit("Added to favorites")
            }
        }
    }

//    fun isFavoriteFlow(detailId: String): Flow<Boolean> {
//        return repository.isFavoriteFlow(detailId)
//    }


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

    private val _vocabs = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabs: StateFlow<List<Vocabulary>> = _vocabs


    fun getAllFavoriteWithDetailFlow() {
        loadJob = viewModelScope.launch {
            _loadingShimmer.value = true
            repository.getAllFavorites().collectLatest {
                _vocabs.value = sortList(it)
                delay(300)
                _loadingShimmer.value = false
            }
        }
    }

    fun searchFavorite(query: String) {
        searchJob = viewModelScope.launch {
            _loadingShimmer.value = true
            repository.searchFavorite(query).collectLatest {
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
                    list.sortedByDescending { it.favoriteTimestamp }
                } else {
                    list.sortedBy { it.favoriteTimestamp }
                }
            }
        }
    }

}
