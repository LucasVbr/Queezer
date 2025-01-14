package fr.univpau.queezer.manager

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.Game
import fr.univpau.queezer.data.GameMode
import fr.univpau.queezer.data.Playlist
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.data.Track
import fr.univpau.queezer.service.DatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class GameManager() {
    private lateinit var databaseService: DatabaseService

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    var countDownManager = CountdownManager(30000L, {}, {})

    var settings: Settings = Settings()
    var playlist: Playlist = Playlist()

    var gameFinished by mutableStateOf(false)
    var currentTrackIndex by mutableIntStateOf(0)
    var score by mutableIntStateOf(0)

    constructor(settings: Settings, playlist: Playlist, onTickTrack: () -> Unit, onFinishTrack: () -> Unit, databaseService: DatabaseService) : this() {
        this.settings = settings
        this.databaseService = databaseService
        this.playlist = Playlist(playlist.title, playlist.tracks.subList(0, settings.numberOfTitles!!))

        this.countDownManager = CountdownManager(duration = 30000L, onTickTimer = onTickTrack, onFinishTimer = { onFinishTrack() })

        for (track in playlist.tracks) {
            if (settings.gameMode == GameMode.TITLE) {
                track.title.answer = Answer.INCORRECT
            }
            if (settings.gameMode == GameMode.ARTIST) {
                track.artist.answer = Answer.INCORRECT
            }
            if (settings.gameMode == GameMode.ALL) {
                track.title.answer = Answer.INCORRECT
                track.artist.answer = Answer.INCORRECT
            }
        }
    }

    fun checkTitleAnswer(currentTrack: Track?, answerTitle: String) {
        if (currentTrack == null) return;

        val simpleTitleAnswer = formatString(answerTitle)
        val simpleTitle = formatString(currentTrack.title.value)

        if (simpleTitle.equals(simpleTitleAnswer, ignoreCase = true)) {
            currentTrack.title.answer = Answer.CORRECT
            score++
        } else {
            currentTrack.title.answer = Answer.INCORRECT
        }
    }

    fun checkArtistAnswer(currentTrack: Track?, answerArtist: String) {
        if (currentTrack == null) return;

        val simpleArtistAnswer = formatString(answerArtist)
        val simpleArtist = formatString(currentTrack.artist.value)

        if (simpleArtist.equals(simpleArtistAnswer, ignoreCase = true)) {
            currentTrack.artist.answer = Answer.CORRECT
            score++
        } else {
            currentTrack.artist.answer = Answer.INCORRECT
        }
    }

    private fun formatString(input: String): String {
        return input
            .trim()
            .lowercase(Locale.ROOT)
            .removeSurrounding("(", ")")
            .removeSurrounding("[", "]")
            .removeSurrounding("{", "}")
            .removeSurrounding("\"", "\"")
            .replace("[^a-zA-Z0-9]".toRegex(), "")
            .trim()
    }

    fun getCurrentTrack(): Track? {
        if (playlist.tracks.isEmpty()) return null;
        return playlist.tracks[currentTrackIndex]
    }

    fun stop() {
        mediaPlayer.release()
        countDownManager.stop()
    }

    fun nextTrack() {
        mediaPlayer.release()

        Log.i("GameManager", "Current track index: $currentTrackIndex")
        Log.i("GameManager", "Playlist size: ${playlist.tracks.size}")

        if (currentTrackIndex + 1 >= playlist.tracks.size) {
            this.gameFinished = true
            return;
        }

        currentTrackIndex++

        mediaPlayer = MediaPlayer().apply {
            setDataSource(playlist.tracks[currentTrackIndex].preview)
            prepare()
            start()
        }

        countDownManager.restart()
    }


    fun start() {
        countDownManager.start()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(playlist.tracks[currentTrackIndex].preview)
            prepare()
            start()
        }
    }

    fun save(context: Context) {
        // Créer un objet Game
        val game = Game(
            id = 0,
            settings = settings,
            playlist = playlist,
            score = score,
            date = Date()
        )

        // Sauvegarder le jeu avec Room
        CoroutineScope(Dispatchers.IO).launch {
            databaseService.gameDao().insert(game)
        }
    }

    fun pause() {
        mediaPlayer.pause()
        countDownManager.stop()
    }
}