package com.example.boliviaslang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boliviaslang.data.AppDatabase
import com.example.boliviaslang.data.SampleData
import com.example.boliviaslang.data.ThemePreferences
import com.example.boliviaslang.data.WordRepository
import com.example.boliviaslang.navigation.AppNavGraph
import com.example.boliviaslang.ui.theme.BoliviaSlangTheme
import com.example.boliviaslang.viewmodel.ThemeViewModel
import com.example.boliviaslang.viewmodel.ViewModelFactory
import com.example.boliviaslang.viewmodel.WordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Aprovecha toda la pantalla en Android 12+, con barras del sistema translúcidas.

        // Inicialización de base de datos y repositorio (patrón simple, sin DI pesado).
        val appScope = MainScope()
        val database = AppDatabase.getDatabase(applicationContext, appScope)
        val repository = WordRepository(database.wordDao())
        val themePreferences = ThemePreferences(applicationContext)
        val factory = ViewModelFactory(repository, themePreferences)

        // Sincroniza palabras nuevas cada vez que se abre la app (sin duplicar ni borrar nada).
        appScope.launch(Dispatchers.IO) {
            repository.syncSampleWords(SampleData.words)
        }

        setContent {
            val wordViewModel: WordViewModel = viewModel(factory = factory)
            val themeViewModel: ThemeViewModel = viewModel(factory = factory)
            val themeMode by themeViewModel.themeMode.collectAsState()

            BoliviaSlangTheme(themeMode = themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(wordViewModel = wordViewModel, themeViewModel = themeViewModel)
                }
            }
        }
    }
}
