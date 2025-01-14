package fr.univpau.queezer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.manager.loadSettings
import fr.univpau.queezer.view.screens.GameScreen
import fr.univpau.queezer.view.screens.HomeScreen
import fr.univpau.queezer.view.screens.ScoreScreen
import fr.univpau.queezer.view.screens.SettingsScreen
import fr.univpau.queezer.service.DatabaseService
import fr.univpau.queezer.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database: DatabaseService by lazy { DatabaseService.getDatabase(this) }

        setContent {
            QueezerApp(database)
        }
    }
}

@Composable
fun QueezerApp(database: DatabaseService) {
    val navController = rememberNavController()
    val gameViewModel = GameViewModel(database.gameDao())

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("game") {
            val context = LocalContext.current
            val settings: Settings = loadSettings(context)
            GameScreen(navController, settings, database)
        }
        composable("custom_settings") { SettingsScreen(navController, saveLocation = "custom_settings") }
        composable("custom_game") {
            val context = LocalContext.current
            val settings: Settings = loadSettings(context, "custom_settings")
            GameScreen(navController, settings, database)
        }

        composable("settings") { SettingsScreen(navController) }
        composable("score") { ScoreScreen(navController, gameViewModel) }
    }
}