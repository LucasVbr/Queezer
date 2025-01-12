package fr.univpau.queezer.manager

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
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
    // var tracks: List<Track> = emptyList()
    var playlist: Playlist = Playlist()
    var gameFinished : Boolean = false;

    var currentTrackIndex: Int = 0;
    var score = 0

    constructor(settings: Settings, playlist: Playlist, onTick: () -> Unit, databaseService: DatabaseService) : this() {
        this.databaseService = databaseService
        this.settings = settings
        this.playlist = playlist

        this.countDownManager = CountdownManager(30000L, onTickTimer = onTick, onFinishTimer = { nextTrack() })

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

        Log.i("GameManager", "Current track: ${currentTrack.title.value}")
        Log.i("GameManager", "Answer: $answerTitle")

        var simpleTitleAnswer = formatString(answerTitle)
        var simpleTitle = formatString(currentTrack.title.value)

        if (simpleTitle.equals(simpleTitleAnswer, ignoreCase = true)) {
            currentTrack.title.answer = Answer.CORRECT
            score++
        } else {
            currentTrack.title.answer = Answer.INCORRECT
        }
    }

    fun checkArtistAnswer(currentTrack: Track?, answerArtist: String) {
        if (currentTrack == null) return;

        Log.i("GameManager", "Current track: ${currentTrack.artist.value}")
        Log.i("GameManager", "Answer: $answerArtist")

        var simpleArtistAnswer = formatString(answerArtist)
        var simpleArtist = formatString(currentTrack.artist.value)

        if (simpleArtist.equals(simpleArtistAnswer, ignoreCase = true)) {
            currentTrack.artist.answer = Answer.CORRECT
            score++
        } else {
            currentTrack.artist.answer = Answer.INCORRECT
        }
    }

    fun formatString(input: String): String {
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


        if (currentTrackIndex >= playlist.tracks.size - 1) {
            return; // TODO vérifier avant meme de lancer la partie si le nombre de titres est suffisant
        }

        if (currentTrackIndex >= settings.numberOfTitles!! - 1) {
            gameFinished = true
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
        Log.i("GameManager", "Next track: ${playlist.tracks[currentTrackIndex].preview}")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(playlist.tracks[currentTrackIndex].preview)
            prepare()
            start()
        }
    }

    fun save(context: Context) {
        // TODO Sauvegarder tout le jeu en base de donnée locale (Room) dans un objet Game

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
}