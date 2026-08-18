package com.example.boliviaslang.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAllWords()
    val favoriteWords: Flow<List<Word>> = wordDao.getFavorites()
    val flashcardWords: Flow<List<Word>> = wordDao.getWordsForFlashcards()

    fun search(query: String): Flow<List<Word>> = wordDao.searchWords(query)

    suspend fun addWord(word: Word) {
        wordDao.insertWord(word)
    }

    /**
     * Compara las palabras "de fábrica" (SampleData) contra las que ya existen en el
     * teléfono del usuario, e inserta solo las que faltan. Así, cuando agregamos más
     * palabras al código y el usuario actualiza la app, las nuevas aparecen automáticamente
     * sin borrar sus favoritos ni las palabras que él mismo agregó.
     */
    suspend fun syncSampleWords(sampleWords: List<Word>) {
        val existingTerms = wordDao.getAllTerms().map { it.trim().lowercase() }.toSet()
        val missing = sampleWords.filter { it.term.trim().lowercase() !in existingTerms }
        if (missing.isNotEmpty()) {
            wordDao.insertAll(missing)
        }
    }

    suspend fun toggleFavorite(word: Word) {
        wordDao.update(word.copy(isFavorite = !word.isFavorite))
    }

    suspend fun markReviewed(word: Word) {
        wordDao.update(word.copy(timesReviewed = word.timesReviewed + 1))
    }
}
