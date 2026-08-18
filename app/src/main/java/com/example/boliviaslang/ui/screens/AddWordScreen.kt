package com.example.boliviaslang.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.boliviaslang.viewmodel.WordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    viewModel: WordViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    var term by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var exampleSentence by remember { mutableStateOf("") }
    var exampleTranslation by remember { mutableStateOf("") }
    var funFact by remember { mutableStateOf("") }

    // El botón de guardar solo se activa cuando lo mínimo indispensable está lleno.
    val isValid = term.isNotBlank() && meaning.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar palabra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Completa los datos de la nueva palabra o expresión. Solo el término y el significado son obligatorios.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = term,
                onValueChange = { term = it },
                label = { Text("Palabra o expresión *") },
                placeholder = { Text("Ej: Chala") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = meaning,
                onValueChange = { meaning = it },
                label = { Text("Significado *") },
                placeholder = { Text("Ej: Sandalia u ojota") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (opcional)") },
                placeholder = { Text("Ej: Comida, Expresión, Objeto...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                label = { Text("Región (opcional)") },
                placeholder = { Text("Ej: Santa Cruz, La Paz, todo el país...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = exampleSentence,
                onValueChange = { exampleSentence = it },
                label = { Text("Oración de ejemplo (opcional)") },
                placeholder = { Text("Ej: Ponte tus chalas que hace calor") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 70.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = exampleTranslation,
                onValueChange = { exampleTranslation = it },
                label = { Text("Explicación del ejemplo (opcional)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 70.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = funFact,
                onValueChange = { funFact = it },
                label = { Text("Dato curioso (opcional)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 70.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    viewModel.addWord(
                        term = term,
                        meaning = meaning,
                        region = region,
                        category = category,
                        exampleSentence = exampleSentence,
                        exampleTranslation = exampleTranslation,
                        funFact = funFact,
                        onDone = onSaved
                    )
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar palabra")
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
