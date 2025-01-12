package fr.univpau.queezer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.univpau.queezer.screen.GameScreen
import fr.univpau.queezer.screen.HomeScreen
import fr.univpau.queezer.screen.ScoreScreen
import fr.univpau.queezer.screen.SettingsScreen
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