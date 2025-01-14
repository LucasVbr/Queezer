package fr.univpau.queezer.view.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextOverflow
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun GameScreen(navController: NavHostController, database: DatabaseService) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings: Settings = loadSettings(context)

    var gameManager by remember { mutableStateOf(GameManager()) }
    var countdown by remember { mutableIntStateOf(30) }

    val titleInput = remember { mutableStateOf("") }
    val artistInput = remember { mutableStateOf("") }

    LaunchedEffect(settings.playlistUrl) {
        val playlist = fetchPlaylist(settings.playlistUrl)

        gameManager = GameManager(
            settings = settings,
            playlist = playlist ?: Playlist(),
            onTickTrack = {
                countdown = gameManager.countDownManager.timeLeft.toInt()
            },
            onFinishTrack = {
                titleInput.value = ""
                artistInput.value = ""

                gameManager.nextTrack()
            },
            databaseService = database
        )

        gameManager.start()
    }

    if (gameManager.getCurrentTrack() == null) {
        LoadingScreen()
    } else if (gameManager.gameFinished) {
        FinishScreen(gameManager, context, navController)
    } else {
        InGameScreen(
            gameManager,
            context,
            coroutineScope,
            navController,
            titleInput,
            artistInput,
            countdown
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InGameScreen(
    gameManager: GameManager,
    context: Context,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    titleInput: MutableState<String>,
    artistInput: MutableState<String>,
    countdown: Int
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "${countdown}sec",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = { Text("${gameManager.currentTrackIndex + 1}/${gameManager.playlist.tracks.size}") },
                actions = { Text("${gameManager.score}pts") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = {
                    gameManager.stop()
                    navController.popBackStack()
                })
                { Text(context.resources.getString(R.string.give_up)) }

                Button(
                    onClick = {
                        titleInput.value = ""
                        artistInput.value = ""
                        gameManager.nextTrack()
                    },
                ) { Text(context.resources.getString(R.string.next)) }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT && gameManager.getCurrentTrack()!!.artist.answer == Answer.UNKNOWN
                    || gameManager.getCurrentTrack()!!.title.answer == Answer.UNKNOWN && gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT
                    || gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT && gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT
                ) {
                    AsyncImage(
                        model = gameManager.getCurrentTrack()!!.album,
                        contentDescription = "Image from URL",
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = gameManager.getCurrentTrack()!!.album,
                        contentDescription = "Image from URL",
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp)
                            .blur(50.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Row {
                    if (gameManager.getCurrentTrack()!!.title.answer == Answer.CORRECT || gameManager.getCurrentTrack()!!.title.answer == Answer.UNKNOWN) {
                        Text(
                            "Titre : ${gameManager.getCurrentTrack()!!.title.value}",
                            fontSize = 20.sp
                        )
                    } else {
                        TextField(
                            value = titleInput.value,
                            onValueChange = {
                                titleInput.value = it;
                                gameManager.checkTitleAnswer(
                                    gameManager.getCurrentTrack(),
                                    titleInput.value
                                )
                            },
                            label = { Text("Titre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Row {
                    if (gameManager.getCurrentTrack()!!.artist.answer == Answer.CORRECT || gameManager.getCurrentTrack()!!.artist.answer == Answer.UNKNOWN) {
                        Text(
                            gameManager.getCurrentTrack()!!.artist.value,
                            fontSize = 20.sp
                        )
                    } else {
                        TextField(
                            value = artistInput.value,
                            onValueChange = {
                                artistInput.value = it;
                                gameManager.checkArtistAnswer(
                                    gameManager.getCurrentTrack(),
                                    artistInput.value
                                )
                            },
                            label = { Text("Artiste") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun FinishScreen(gameManager: GameManager, context: Context, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Partie terminée !", fontSize = 24.sp)
        Text("Score : ${gameManager.score}", fontSize = 20.sp)
        Button(onClick = {
            gameManager.save(context)
            gameManager.stop()
            navController.popBackStack()
        }) { Text(context.resources.getString(R.string.back)) }
    }
}
