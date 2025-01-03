package fr.univpau.queezer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.univpau.queezer.screen.GameScreen
import fr.univpau.queezer.screen.HomeScreen
import fr.univpau.queezer.screen.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QueezerApp()
        }
    }
}

@Composable
fun QueezerApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("game") { GameScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}