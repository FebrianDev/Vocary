package com.febriandev.vocary.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.SearchVocabularyEntity
import com.febriandev.vocary.data.repository.SearchVocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVocabularyViewModel @Inject constructor(private val searchVocabularyRepository: SearchVocabularyRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun fetchDefinition(word: String) {
        viewModelScope.launch {
            if (word.isEmpty()) {
                // langsung clear result + stop loading
                _uiState.update { it.copy(isLoading = false, result = emptyList()) }
                return@launch
            }

            // set loading dan kosongkan dulu biar UI clear
            _uiState.update { it.copy(isLoading = true, result = emptyList()) }

            val data = searchVocabularyRepository.searchVocabulary(word.trim())

            _uiState.update {
                it.copy(
                    isLoading = false,
                    result = data
                )
            }
        }
    }

}

data class SearchUiState(
    val isLoading: Boolean = false,
    val result: List<SearchVocabularyEntity> = emptyList()
)