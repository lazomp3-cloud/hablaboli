package com.example.boliviaslang.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.ui.components.FlashcardComponent
import com.example.boliviaslang.ui.components.rememberSpeaker
import com.example.boliviaslang.viewmodel.WordViewModel

@Composable
fun FlashcardScreen(viewModel: WordViewModel) {
    val words by viewModel.flashcardWords.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    val speak = rememberSpeaker()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Modo Flashcards", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Toca la tarjeta para ver el significado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

        if (words.isEmpty()) {
            CircularProgressIndicator()
        } else {
            val word = words[currentIndex % words.size]

            AnimatedContent(
                targetState = word.id,
                transitionSpec = {
                    (fadeIn(tween(250)) togetherWith fadeOut(tween(150)))
                },
                label = "flashcardTransition"
            ) { _ ->
                FlashcardComponent(
                    word = word,
                    onPlaySound = { speak(word.exampleSentence) }
                )
            }

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedIconButton(onClick = {
                    currentIndex = if (currentIndex == 0) words.size - 1 else currentIndex - 1
                }) {
                    Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Anterior")
                }

                Text(
                    text = "${(currentIndex % words.size) + 1} / ${words.size}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                FilledIconButton(onClick = {
                    viewModel.markReviewed(word)
                    currentIndex = (currentIndex + 1) % words.size
                }) {
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Siguiente")
                }
            }
        }
    }
}
