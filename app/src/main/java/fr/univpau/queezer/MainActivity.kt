package fr.univpau.queezer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable("game") { GameScreen(navController, database) }
        composable("settings") { SettingsScreen(navController) }
        composable("score") { ScoreScreen(navController, gameViewModel) }
    }
}