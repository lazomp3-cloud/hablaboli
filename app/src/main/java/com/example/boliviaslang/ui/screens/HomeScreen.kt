package com.example.boliviaslang.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.data.Word
import com.example.boliviaslang.ui.components.WordCard
import com.example.boliviaslang.ui.components.rememberSpeaker
import com.example.boliviaslang.viewmodel.WordViewModel

@Composable
fun HomeScreen(
    viewModel: WordViewModel,
    onWordClick: (Word) -> Unit,
    onGoToFlashcards: () -> Unit,
    onGoToSearch: () -> Unit
) {
    val words by viewModel.allWords.collectAsState()
    val speak = rememberSpeaker()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Aprende el habla boliviana de forma divertida",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilledTonalButton(onClick = onGoToSearch, modifier = Modifier.weight(1f)) {
                    Text("Buscar palabra")
                }
                Button(onClick = onGoToFlashcards, modifier = Modifier.weight(1f)) {
                    Text("Modo Flashcards")
                }
            }
        }

        Text(
            text = "Palabras para explorar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(words, key = { _, word -> word.id }) { index, word ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300, delayMillis = index * 40)) +
                            slideInVertically(tween(300, delayMillis = index * 40)) { it / 4 }
                ) {
                    WordCard(
                        word = word,
                        onClick = { onWordClick(word) },
                        onToggleFavorite = { viewModel.toggleFavorite(word) },
                        onPlaySound = { speak(word.exampleSentence) }
                    )
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
