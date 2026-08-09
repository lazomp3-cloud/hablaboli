package com.example.boliviaslang.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAllWords()
    val favoriteWords: Flow<List<Word>> = wordDao.getFavorites()
    val flashcardWords: Flow<List<Word>> = wordDao.getWordsForFlashcards()

    fun search(query: String): Flow<List<Word>> = wordDao.searchWords(query)

    suspend fun toggleFavorite(word: Word) {
        wordDao.update(word.copy(isFavorite = !word.isFavorite))
    }

    suspend fun markReviewed(word: Word) {
        wordDao.update(word.copy(timesReviewed = word.timesReviewed + 1))
    }
}
