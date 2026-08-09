package com.example.boliviaslang.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM words ORDER BY term ASC")
    fun getAllWords(): Flow<List<Word>>

    // Buscador predictivo: filtra por término o significado a medida que el usuario escribe.
    @Query(
        """SELECT * FROM words 
           WHERE term LIKE '%' || :query || '%' 
              OR meaning LIKE '%' || :query || '%' 
           ORDER BY term ASC"""
    )
    fun searchWords(query: String): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE isFavorite = 1 ORDER BY term ASC")
    fun getFavorites(): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: Int): Word?

    @Query("SELECT * FROM words ORDER BY RANDOM()")
    fun getWordsForFlashcards(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Word>)

    @Update
    suspend fun update(word: Word)

    @Query("SELECT COUNT(*) FROM words")
    suspend fun count(): Int
}
