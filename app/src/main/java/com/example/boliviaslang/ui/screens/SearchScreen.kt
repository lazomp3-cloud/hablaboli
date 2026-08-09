package com.example.boliviaslang.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.data.Word
import com.example.boliviaslang.ui.components.WordCard
import com.example.boliviaslang.ui.components.rememberSpeaker
import com.example.boliviaslang.viewmodel.WordViewModel

@Composable
fun SearchScreen(
    viewModel: WordViewModel,
    onWordClick: (Word) -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val speak = rememberSpeaker()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(text = "Buscar", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ej: Chala, Yapa, Cambita...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.extraLarge
        )

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(visible = results.isEmpty() && query.isNotBlank(), enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = "No encontramos '$query'. Prueba con otra palabra o expresión.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(results, key = { it.id }) { word ->
                WordCard(
                    word = word,
                    onClick = { onWordClick(word) },
                    onToggleFavorite = { viewModel.toggleFavorite(word) },
                    onPlaySound = { speak(word.exampleSentence) }
                )
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
