package com.example.boliviaslang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boliviaslang.data.ThemePreferences
import com.example.boliviaslang.data.WordRepository

class ViewModelFactory(
    private val wordRepository: WordRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WordViewModel::class.java) ->
                WordViewModel(wordRepository) as T
            modelClass.isAssignableFrom(ThemeViewModel::class.java) ->
                ThemeViewModel(themePreferences) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
