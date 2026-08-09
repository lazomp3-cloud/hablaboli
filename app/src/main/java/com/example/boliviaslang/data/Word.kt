package com.example.boliviaslang.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una palabra o expresión típica boliviana.
 */
@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val term: String,               // Ej: "Chala"
    val meaning: String,             // Significado en español neutro
    val region: String,              // Región de Bolivia donde se usa más
    val category: String,            // Ej: "Objeto", "Comida", "Expresión"
    val exampleSentence: String,     // Oración de ejemplo cotidiano
    val exampleTranslation: String,  // Traducción/explicación de la oración
    val funFact: String = "",        // Dato curioso opcional
    val isFavorite: Boolean = false,
    val timesReviewed: Int = 0       // Para el modo flashcards (repetición espaciada simple)
)
