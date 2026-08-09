package com.example.boliviaslang.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.data.Word
import com.example.boliviaslang.ui.components.rememberSpeaker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    word: Word,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val speak = rememberSpeaker()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(word.term) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (word.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            AnimatedSection(delayMillis = 0) {
                Text(word.term, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                AssistChip(onClick = {}, label = { Text(word.category) })
            }

            Spacer(Modifier.height(20.dp))

            AnimatedSection(delayMillis = 80) {
                SectionCard(title = "Significado") {
                    Text(word.meaning, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedSection(delayMillis = 160) {
                SectionCard(title = "Ejemplo de uso cotidiano") {
                    Text(
                        text = "\"${word.exampleSentence}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = word.exampleTranslation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    FilledTonalButton(onClick = { speak(word.exampleSentence) }) {
                        Icon(Icons.Filled.VolumeUp, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Escuchar ejemplo")
                    }
                }
            }

            if (word.funFact.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                AnimatedSection(delayMillis = 240) {
                    SectionCard(title = "¿Sabías que...?") {
                        Text(word.funFact, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            AnimatedSection(delayMillis = 300) {
                SectionCard(title = "Región de uso") {
                    Text(word.region, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun AnimatedSection(delayMillis: Int, content: @Composable ColumnScope.() -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(350, delayMillis)) + slideInVertically(tween(350, delayMillis)) { it / 3 }
    ) {
        Column { content() }
    }
}
