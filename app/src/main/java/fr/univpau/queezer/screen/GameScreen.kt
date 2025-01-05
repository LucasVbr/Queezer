package fr.univpau.queezer.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.manager.loadSettings
import coil.compose.AsyncImage
import fr.univpau.queezer.manager.GameManager
import fr.univpau.queezer.manager.fetchAndFormatPlaylist

@Composable
fun GameScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings: Settings = loadSettings(context)

    var gameManager by remember { mutableStateOf(GameManager(settings, emptyList())) }
    var currentTrack by remember { mutableStateOf(gameManager.getCurrentTrack()) }

    LaunchedEffect(settings.playlistUrl) {
        val tracks = fetchAndFormatPlaylist(settings.playlistUrl).shuffled()
        gameManager = GameManager(settings, tracks)

        currentTrack = gameManager.getCurrentTrack()
        gameManager.start()
        Log.i("GameScreen", gameManager.getCurrentTrack().toString())
    }

    // État de l'utilisateur et des éléments du jeu
    val userInput = remember { mutableStateOf("") }

    // Affichage de l'interface
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (currentTrack == null) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            Text("Score : ${gameManager.score}", fontSize = 24.sp)
            Text("Temps restant : ${gameManager.countDownManager.timeLeft}sec", fontSize = 20.sp)

            AsyncImage(
                model = currentTrack!!.album,
                contentDescription = "Image from URL",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text("Titre : ${currentTrack!!.title.value}", fontSize = 20.sp)
            Text("Artiste : ${currentTrack!!.artist.value}", fontSize = 20.sp)

            // Champ de texte pour entrer la proposition
            TextField(
                value = userInput.value,
                onValueChange = { userInput.value = it },
                label = { Text("Titre / Artiste") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            // Bouton Valider la réponse
            Button(
                onClick = {
                    userInput.value = "" // Réinitialiser le champ de texte
                }
            ) { Text(context.resources.getString(R.string.submit)) }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        userInput.value = "" // Réinitialiser le champ de texte
                        gameManager.nextTrack()
                        currentTrack = gameManager.getCurrentTrack()
                    },
                ) { Text(context.resources.getString(R.string.skip)) }

                Button(onClick = {
                    gameManager.stop()
                    navController.popBackStack()
                })
                { Text(context.resources.getString(R.string.give_up)) }
            }
        }
    }
}
