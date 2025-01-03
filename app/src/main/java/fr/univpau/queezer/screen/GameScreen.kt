package fr.univpau.queezer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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

@Composable
fun GameScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings: Settings = loadSettings(context)

    // val gameManager: GameManager = remember { GameManager(settings) }

    // LaunchedEffect(gameManager) {
    //     gameManager.loadTracks()
    //     gameManager.start()
    // }

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

        Text("Score : 0", fontSize = 24.sp)

        Text("Temps restant : 30sec", fontSize = 20.sp)

        // Affiche une image a partir d'une url
        AsyncImage(
            model = "https://api.deezer.com/album/382921287/image",
            contentDescription = "Image from URL",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .blur(30.dp)
            ,
            contentScale = ContentScale.Crop
        )

        Text("Titre : Légende Vivante", fontSize = 20.sp)
        Text("Artiste : Lorenzo", fontSize = 20.sp)

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
                },
            ) { Text(context.resources.getString(R.string.skip)) }

            Button(onClick = { navController.popBackStack() })
            { Text(context.resources.getString(R.string.give_up)) }
        }
    }
}
