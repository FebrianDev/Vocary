package com.febriandev.vocary.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.data.repository.GenerateVocabRepository
import com.febriandev.vocary.data.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVocabularyViewModel @Inject constructor(private val repository: GenerateVocabRepository) :
    ViewModel() {

    var isLoading by mutableStateOf(false)
    var result by mutableStateOf<List<VocabularyEntity>?>(null)
    var errorMessage by mutableStateOf<String?>(null)


    fun fetchDefinition(word:String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            result = null

//            val vocabulary =repository.getWord(word.trim())
//result = vocabulary
//            res.onSuccess {
//                val filter = it.filter { it.phonetic != null || it.phonetics.any { phonetic -> phonetic.text != null } }
//                result = filter
//            }.onFailure {
//                errorMessage = it.message ?: "Unknown error"
//            }
//
//            isLoading = false
        }
    }
}