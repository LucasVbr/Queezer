package fr.univpau.queezer.manager

import android.media.MediaPlayer
import android.util.Log
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.GameMode
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.data.Track
import java.util.Locale

class GameManager() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    val countDownManager = CountdownManager(30000L, onFinish = ::nextTrack)

    var settings: Settings = Settings()
    var tracks: List<Track> = emptyList()

    var currentTrackIndex: Int = 0;
    var score = 0

    constructor(settings: Settings, tracks: List<Track>) : this() {
        this.settings = settings
        this.tracks = tracks

        for (track in tracks) {
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
            .toLowerCase(Locale.ROOT)
            .removeSurrounding("(", ")")
            .removeSurrounding("[", "]")
            .removeSurrounding("{", "}")
            .removeSurrounding("\"", "\"")
            .replace("[^a-zA-Z0-9]".toRegex(), "")
            .trim()
    }

    fun getCurrentTrack(): Track? {
        if (tracks.isEmpty()) return null;
        return tracks[currentTrackIndex]
    }

    fun stop() {
        mediaPlayer.release()
        // countDownManager.stop()
    }

    fun nextTrack() {
        mediaPlayer.release()


        if (currentTrackIndex >= tracks.size - 1) {
            return; // TODO vérifier avant meme de lancer la partie si le nombre de titres est suffisant
        }

        if (currentTrackIndex >= settings.numberOfTitles!! - 1) {
            return;
        }

        currentTrackIndex++

        mediaPlayer = MediaPlayer().apply {
            setDataSource(tracks[currentTrackIndex].preview)
            prepare()
            start()
        }

        // countDownManager.restart()
    }


    fun start() {
        // countDownManager.start()
        Log.i("GameManager", "Next track: ${tracks[currentTrackIndex].preview}")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tracks[currentTrackIndex].preview)
            prepare()
            start()
        }
    }
}