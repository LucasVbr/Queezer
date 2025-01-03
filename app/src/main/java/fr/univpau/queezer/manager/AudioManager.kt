package fr.univpau.queezer.manager

import android.media.MediaPlayer

class AudioManager {
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    fun play(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            prepare()
            start()
        }
    }

    fun stop() {
        mediaPlayer.release()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}