package com.example.boliviaslang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boliviaslang.data.Word
import com.example.boliviaslang.data.WordRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // --- Búsqueda predictiva ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(kotlinx.coroutines.FlowPreview::class)
    val searchResults: StateFlow<List<Word>> = _searchQuery
        .debounce(200) // Espera breve mientras el usuario escribe, evita búsquedas excesivas.
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allWords else repository.search(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWords: StateFlow<List<Word>> = repository.allWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteWords: StateFlow<List<Word>> = repository.favoriteWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcardWords: StateFlow<List<Word>> = repository.flashcardWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(word: Word) {
        viewModelScope.launch { repository.toggleFavorite(word) }
    }

    // --- Agregar nueva palabra desde la app ---
    fun addWord(
        term: String,
        meaning: String,
        region: String,
        category: String,
        exampleSentence: String,
        exampleTranslation: String,
        funFact: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.addWord(
                Word(
                    term = term.trim(),
                    meaning = meaning.trim(),
                    region = region.trim().ifBlank { "Todo el país" },
                    category = category.trim().ifBlank { "General" },
                    exampleSentence = exampleSentence.trim(),
                    exampleTranslation = exampleTranslation.trim(),
                    funFact = funFact.trim()
                )
            )
            onDone()
        }
    }

    fun markReviewed(word: Word) {
        viewModelScope.launch { repository.markReviewed(word) }
    }
}
