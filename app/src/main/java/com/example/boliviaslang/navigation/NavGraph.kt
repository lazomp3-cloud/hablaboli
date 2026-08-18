package com.example.boliviaslang.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.compose.composable
import com.example.boliviaslang.ui.screens.*
import com.example.boliviaslang.viewmodel.ThemeViewModel
import com.example.boliviaslang.viewmodel.WordViewModel

private object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val FLASHCARDS = "flashcards"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{wordId}"
    const val ADD_WORD = "add_word"
    fun detail(id: Int) = "detail/$id"
}

private data class BottomItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomItems = listOf(
    BottomItem(Routes.HOME, "Inicio", Icons.Filled.Home),
    BottomItem(Routes.SEARCH, "Buscar", Icons.Filled.Search),
    BottomItem(Routes.FLASHCARDS, "Flashcards", Icons.Filled.Style),
    BottomItem(Routes.SETTINGS, "Ajustes", Icons.Filled.Settings)
)

@Composable
fun AppNavGraph(
    wordViewModel: WordViewModel,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination

            // Oculta la barra inferior en las pantallas de detalle y "agregar palabra" para más inmersión.
            val showBar = currentRoute?.route != Routes.DETAIL && currentRoute?.route != Routes.ADD_WORD
            if (showBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val selected = currentRoute?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 6 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 6 } }
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = wordViewModel,
                    onWordClick = { navController.navigate(Routes.detail(it.id)) },
                    onGoToFlashcards = { navController.navigate(Routes.FLASHCARDS) },
                    onGoToSearch = { navController.navigate(Routes.SEARCH) },
                    onGoToAddWord = { navController.navigate(Routes.ADD_WORD) }
                )
            }
            composable(Routes.ADD_WORD) {
                AddWordScreen(
                    viewModel = wordViewModel,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(Routes.SEARCH) {
                SearchScreen(
                    viewModel = wordViewModel,
                    onWordClick = { navController.navigate(Routes.detail(it.id)) }
                )
            }
            composable(Routes.FLASHCARDS) {
                FlashcardScreen(viewModel = wordViewModel)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(viewModel = themeViewModel)
            }
            composable(Routes.DETAIL) { backStackEntry ->
                val wordId = backStackEntry.arguments?.getString("wordId")?.toIntOrNull()
                val words by wordViewModel.allWords.collectAsState()
                val word = words.find { it.id == wordId }
                if (word != null) {
                    WordDetailScreen(
                        word = word,
                        onBack = { navController.popBackStack() },
                        onToggleFavorite = { wordViewModel.toggleFavorite(word) }
                    )
                }
            }
        }
    }
}
