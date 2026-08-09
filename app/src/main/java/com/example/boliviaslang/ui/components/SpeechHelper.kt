package com.example.boliviaslang.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Provee una función lambda para "leer en voz alta" cualquier texto (los ejemplos de uso),
 * usando el motor de Text-to-Speech nativo de Android. No requiere archivos de audio.
 */
@Composable
fun rememberSpeaker(): (String) -> Unit {
    val context = LocalContext.current
    val ttsRef = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Español latinoamericano; si no está disponible, el sistema usa el default.
                ttsRef.value?.language = Locale("es", "BO")
            }
        }
        ttsRef.value = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    return { text ->
        ttsRef.value?.let { tts ->
            tts.language = Locale("es", "BO")
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_${text.hashCode()}")
        }
    }
}
