package fr.univpau.queezer.screen

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.univpau.queezer.R
import fr.univpau.queezer.data.GameMode
import fr.univpau.queezer.manager.loadSettings
import fr.univpau.queezer.manager.saveSettings
import java.net.URL

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings = remember { mutableStateOf(loadSettings(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre des paramètres
        Text(
            text = context.resources.getString(R.string.settings),
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Paramètre : Playlist à utiliser
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                label = { Text(context.resources.getString(R.string.playlist_url_label)) },
                placeholder = { Text(context.resources.getString(R.string.playlist_url_hint)) },
                modifier = Modifier.fillMaxWidth(),
                value = settings.value.playlistUrl,
                onValueChange = { settings.value = settings.value.copy(playlistUrl = it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            )
        }

        // Paramètre : Nombre de titres dans une partie
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                label = { Text(context.resources.getString(R.string.tracks_count_label)) },
                modifier = Modifier.fillMaxWidth(1f),
                value = settings.value.numberOfTitles?.toString() ?: "",
                onValueChange = {
                    settings.value = settings.value.copy(numberOfTitles = it.toIntOrNull())
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
        }

        // Paramètre : Mode de jeu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Mode de jeu", fontSize = 18.sp)
                val gameModes = context.resources.getStringArray(R.array.game_modes)
                gameModes.forEachIndexed { index, label ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = settings.value.gameMode.ordinal == index,
                            onClick = {
                                settings.value =
                                    settings.value.copy(gameMode = GameMode.entries[index])
                            }
                        )
                        Text(text = label)
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
            ) { Text(context.resources.getString(R.string.back)) }

            Button(
                onClick = {
                    try {
                        settings.value.validate(context)
                        saveSettings(context, settings.value)
                        navController.popBackStack()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                },
            ) {
                Text(context.resources.getString(R.string.submit))
            }
        }
    }
}
