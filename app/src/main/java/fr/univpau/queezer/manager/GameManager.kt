package fr.univpau.queezer.manager

import android.media.MediaPlayer
import android.util.Log
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.data.Track

class GameManager(var settings: Settings, val tracks: List<Track>) {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    val countDownManager = CountdownManager(30000L, onFinish = ::nextTrack)

    var currentTrackIndex: Int = 0;
    var score = 0

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