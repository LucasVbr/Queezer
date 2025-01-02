package fr.univpau.queezer

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.runBlocking

@Composable
fun GameScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loadedSettings = loadSettings(context)

    var selectedGameMode by remember { mutableStateOf(loadedSettings.gameMode) }
    val numberOfTitles by remember { mutableIntStateOf(loadedSettings.numberOfTitles.toInt()) }
    val playlistUrl by remember { mutableStateOf(loadedSettings.playlistUrl) }
    var tracks = remember { mutableStateOf(emptyList<Track>()) }

    LaunchedEffect(playlistUrl) {
        tracks = fetchTracks(playlistUrl)
    }

    val score = remember { mutableIntStateOf(0) }
    val remainingTitles = remember { mutableIntStateOf(numberOfTitles) }  // Exemple avec 5 titres restants
    val userInput = remember { mutableStateOf("") }
    // val albumCover: Painter = painterResource(id = R.drawable.album_cover) // Remplacez par une ressource valide d'album
    val isCoverVisible = remember { mutableStateOf(false) }
    val totalTime = 30000L  // 30 secondes
    var timeLeft by remember { mutableStateOf(totalTime / 1000) }

    var currentTrackIndex by remember { mutableIntStateOf(0) }

    // Timer de 30 secondes
    LaunchedEffect(Unit) {
        object : CountDownTimer(totalTime, 1000) { // Tick toutes les secondes
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished / 1000 // Mettre à jour en secondes
            }

            override fun onFinish() {
                timeLeft = 0 // Compte à rebours terminé
                currentTrackIndex += 1 // Passer à la chanson suivante
            }
        }.start()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Score
        Text("Score : ${score.intValue}", fontSize = 24.sp)

        // Nombre de titres restants
        Text("Titres restants : ${remainingTitles.intValue}", fontSize = 20.sp)

        // Timer
        Text("Temps restant : $timeLeft s", fontSize = 20.sp)

        // Affichage de la couverture de l'album
        if (isCoverVisible.value) {
            // Image(painter = albumCover, contentDescription = "Cover", modifier = Modifier.fillMaxWidth())
        } else {
            Text("Couverture cachée", fontSize = 18.sp)
        }

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
                // Logique pour valider la réponse, en vérifiant la casse et en ajustant le score
                val correctAnswer = "Titre Correct" // Exemple, il faut remplacer par la bonne réponse
                if (userInput.value.trim().equals(correctAnswer, ignoreCase = true)) {
                    score.value += 1  // Ajouter 10 points pour une bonne réponse
                }
                remainingTitles.value -= 1
                userInput.value = ""  // Réinitialiser le champ de texte
                // Réinitialiser ou ajuster le timer si nécessaire
            }
        ) {
            Text("Valider")
        }

        // Bouton Passer
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    // Logique pour passer la chanson
                    remainingTitles.value -= 1
                    // Vous pouvez réinitialiser le timer, ou passer à la chanson suivante
                },
            ) {
                Text("Passer")
            }

            // Bouton Abandonner
            Button(
                onClick = {
                    // Logique pour abandonner, peut-être retour à l'écran d'accueil
                    navController.popBackStack()
                },
            ) {
                Text("Abandonner")
            }
        }
    }
}