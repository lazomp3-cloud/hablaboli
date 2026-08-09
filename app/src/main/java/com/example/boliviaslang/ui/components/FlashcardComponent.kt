package com.example.boliviaslang.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.data.Word

/**
 * Tarjeta de estudio con animación de volteo (flip 3D):
 * - Cara frontal: muestra el término boliviano.
 * - Cara trasera: muestra el significado y un ejemplo de uso con botón de audio.
 * Toca la tarjeta para voltearla.
 */
@Composable
fun FlashcardComponent(
    word: Word,
    onPlaySound: () -> Unit,
    modifier: Modifier = Modifier
) {
    var flipped by remember(word.id) { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 450),
        label = "flipRotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(28.dp))
    ) {
        if (rotation <= 90f) {
            // --- Cara frontal ---
            FlashcardFace(
                background = Brush.linearGradient(
                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                ),
                onClick = { flipped = !flipped }
            ) {
                Text(
                    text = word.term,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Toca para descubrir el significado",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            // --- Cara trasera (espejada para que se lea correctamente al terminar el giro) ---
            FlashcardFace(
                background = Brush.linearGradient(
                    listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)
                ),
                onClick = { flipped = !flipped },
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            ) {
                Text(
                    text = word.meaning,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "\"${word.exampleSentence}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                FilledTonalIconButton(onClick = onPlaySound) {
                    Icon(Icons.Filled.PlayCircle, contentDescription = "Escuchar ejemplo")
                }
            }
        }
    }
}

@Composable
private fun FlashcardFace(
    background: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
        Spacer(Modifier.weight(1f))
        TextButton(onClick = onClick) { Text("Voltear tarjeta") }
    }
}
