package fr.univpau.queezer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loadedSettings = loadSettings(context)

    var selectedGameMode by remember { mutableStateOf(loadedSettings.gameMode) }
    var numberOfTitles by remember { mutableStateOf(loadedSettings.numberOfTitles) }
    var playlistUrl by remember { mutableStateOf(loadedSettings.playlistUrl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre des paramètres
        Text(
            text = "Paramètres",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Paramètre : Playlist à utiliser
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = playlistUrl,
                onValueChange = { playlistUrl = it },
                label = { Text("URL de la playlist") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Paramètre : Nombre de titres dans une partie
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = numberOfTitles,
                onValueChange = { numberOfTitles = it },
                label = { Text("Nombre de titres dans une partie") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(1f)
            )
        }

        // Paramètre : Mode de jeu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Mode de jeu", fontSize = 18.sp)
                gameModes.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedGameMode == option,
                            onClick = { selectedGameMode = option }
                        )
                        Text(text = option)
                    }
                }
            }
        }

        // Bouton pour revenir à l'écran d'accueil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { navController.popBackStack() },
            ) {
                Text("Retour")
            }

            Button(
                onClick = {
                    saveSettings(context, selectedGameMode, numberOfTitles, playlistUrl)
                    navController.popBackStack()
                },
            ) {
                Text("Valider")
            }
        }
    }
}