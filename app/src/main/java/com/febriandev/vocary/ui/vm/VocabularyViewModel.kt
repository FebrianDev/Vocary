package com.febriandev.vocary.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.SrsStatus
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.data.repository.VocabularyRepository
import com.febriandev.vocary.domain.Vocabulary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(private val repository: VocabularyRepository) :
    ViewModel() {

    private val _vocabs = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabs: StateFlow<List<Vocabulary>> = _vocabs

    private val _uiMessage = Channel<String>(Channel.BUFFERED)
    val uiMessage = _uiMessage.receiveAsFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading


    init {
        getAllVocabulary()
    }

    fun getAllVocabulary() {
        viewModelScope.launch {

            val now = System.currentTimeMillis()

            val vocabList = repository.getAllVocabulary(now)
            val finalList = vocabList.map { vocab ->
                if (vocab.srsDueDate <= now) {
                    vocab.copy(srsStatus = SrsStatus.NEW)
                } else vocab
            }

            _vocabs.value = finalList

        }
    }

    suspend fun getCountNewOrNope(): Int {
        return repository.getCountNewOrNope()
    }

    fun updateNote(id: String, note: String) = viewModelScope.launch {
        repository.updateNote(id, note)

        // Update state di memori supaya UI realtime
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                vocab.copy(note = note)
            } else vocab
        }
        _vocabs.value = updatedList

        // Kirim UI message
        _uiMessage.send("Note for \"${updatedList.first { it.id == id }.word}\" updated")
    }

    fun addReport(
        id: String
    ) = viewModelScope.launch {

        val reportedWord = _vocabs.value.firstOrNull { it.id == id }?.word ?: ""

        // Update DB
        repository.addReport(id)

        // Hapus dari list
        _vocabs.value = _vocabs.value.filter { it.id != id }

        // Kirim pesan
        _uiMessage.send("\"$reportedWord\" successfully reported")
    }

    fun updateSrsStatus(id: String, status: SrsStatus) = viewModelScope.launch {
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                // Hitung dueDate berdasarkan status
                val now = System.currentTimeMillis()
                val intervalMillis = when (status) {
                    SrsStatus.NOPE -> 10 * 60 * 1000L       // 10 menit
                    SrsStatus.MAYBE -> 60 * 60 * 1000L      // 1 jam
                    SrsStatus.KNOWN -> 24 * 60 * 60 * 1000L // 1 hari
                    SrsStatus.NEW -> 5 * 60 * 1000L         // default
                }
                val dueDate = now + intervalMillis

                // Simpan ke DB
                repository.updateSrs(id, status, dueDate)

                // Kirim pesan ke UI
                _uiMessage.send(
                    when (status) {
                        SrsStatus.NOPE -> "\"${vocab.word}\" will be reviewed again soon"
                        SrsStatus.MAYBE -> "\"${vocab.word}\" will be reviewed later"
                        SrsStatus.KNOWN -> "Great! \"${vocab.word}\" will be reviewed much later"
                        SrsStatus.NEW -> "\"${vocab.word}\" has not been answered yet"
                    }
                )

                // Update state di UI
                vocab.copy(
                    srsStatus = status,
                    srsDueDate = dueDate,
                    // srsLastReviewed = now
                )
            } else vocab
        }

        _vocabs.value = updatedList
    }


    fun toggleFavorite(id: String) = viewModelScope.launch {
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                val newFavorite = !vocab.isFavorite

                // Simpan ke DB
                if (newFavorite) {
                    repository.addToFavorite(id)
                    _uiMessage.send("\"${vocab.word}\" added to favorites")
                } else {
                    repository.removeFromFavorite(id)
                    _uiMessage.send("\"${vocab.word}\" removed from favorites")
                }

                // Update di UI state
                vocab.copy(isFavorite = newFavorite)
            } else vocab
        }

        _vocabs.value = updatedList
    }

    fun addToFavorite(id: String) = viewModelScope.launch {
        val updatedList = _vocabs.value.map { vocab ->
            if (vocab.id == id) {
                if (!vocab.isFavorite) {
                    repository.addToFavorite(id)
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

    private val addedToHistoryIds = mutableSetOf<String>()

    fun tryAddToHistory(vocab: Vocabulary) = viewModelScope.launch {
        if (!vocab.isHistory && addedToHistoryIds.add(vocab.id)) {
            repository.addToHistory(vocab.id)
        }
    }

    fun insertIfNotExists(vocabulary: VocabularyEntity) {
        viewModelScope.launch {
            val existing = repository.getVocabularyByWord(vocabulary.word)
            if (existing == null) {
                repository.insertVocabulary(vocabulary)
                _uiMessage.send("\"${vocabulary.word}\" successfully added to my own word")
            } else {
                _uiMessage.send("\"${vocabulary.word}\" already have added to my own word")
            }
        }
    }

    suspend fun deleteAllData() {
        //viewModelScope.launch {
            repository.deleteAllData()
        //}
    }

}