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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.Playlist
import fr.univpau.queezer.manager.GameManager
import fr.univpau.queezer.manager.fetchPlaylist
import fr.univpau.queezer.service.DatabaseService
import kotlinx.coroutines.launch

@Composable
fun GameScreen(navController: NavHostController, database: DatabaseService) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings: Settings = loadSettings(context)

    var gameManager by remember { mutableStateOf(GameManager(settings, Playlist(), {}, database)) }
    var countdown by remember { mutableIntStateOf(30) }

    LaunchedEffect(settings.playlistUrl) {
        val playlist = fetchPlaylist(settings.playlistUrl)
        gameManager = GameManager(settings, playlist ?: Playlist(), { countdown = gameManager.countDownManager.timeLeft.toInt() }, database)

        gameManager.start()
        Log.i("GameScreen", gameManager.getCurrentTrack().toString())
    }

    // État de l'utilisateur et des éléments du jeu
    val titleInput = remember { mutableStateOf("") }
    val artistInput = remember { mutableStateOf("") }

    // Affichage de l'interface
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (gameManager.getCurrentTrack() == null) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else if (gameManager.gameFinished) {
            Text("Partie terminée !", fontSize = 24.sp)
            Text("Score : ${gameManager.score}", fontSize = 20.sp)
            Button(onClick = {

                coroutineScope.launch {
                    gameManager.save(context)
                    gameManager.stop()
                    navController.popBackStack()
                }
            }) { Text(context.resources.getString(R.string.back)) }
        } else {
            Text("Score : ${gameManager.score}", fontSize = 24.sp)
            Text("Temps restant : ${countdown}sec", fontSize = 20.sp)

            if (gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT && gameManager.getCurrentTrack()!!.artist.answer == Answer.UNKNOWN
                || gameManager.getCurrentTrack()!!.title.answer == Answer.UNKNOWN && gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT
                || gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT && gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT) {
                AsyncImage(
                    model = gameManager.getCurrentTrack()!!.album,
                    contentDescription = "Image from URL",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                    ,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = gameManager.getCurrentTrack()!!.album,
                    contentDescription = "Image from URL",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .blur(50.dp)
                    ,
                    contentScale = ContentScale.Crop
                )
            }

            Row {
                if (gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT || gameManager.getCurrentTrack()!!.title.answer == Answer.UNKNOWN) {
                    Text("Titre : ${gameManager.getCurrentTrack()!!.title.value}", fontSize = 20.sp)
                } else {
                    TextField(
                        value = titleInput.value,
                        onValueChange = {
                            titleInput.value = it;
                            gameManager.checkTitleAnswer(gameManager.getCurrentTrack(), titleInput.value)
                                        },
                        label = { Text("Titre") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row {
                if (gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT || gameManager.getCurrentTrack()!!.artist.answer == Answer.UNKNOWN) {
                    Text("Artiste : ${gameManager.getCurrentTrack()!!.artist.value}", fontSize = 20.sp)
                } else {
                    TextField(
                        value = artistInput.value,
                        onValueChange = { artistInput.value = it; gameManager.checkArtistAnswer(gameManager.getCurrentTrack(), artistInput.value) },
                        label = { Text("Artiste") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        titleInput.value = ""
                        artistInput.value = ""
                        gameManager.nextTrack()
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
