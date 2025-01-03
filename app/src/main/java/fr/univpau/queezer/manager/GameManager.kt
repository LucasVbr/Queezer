package fr.univpau.queezer.manager

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.data.Track
import java.net.HttpURLConnection
import java.net.URL

class GameManager(var settings: Settings) {

    val audioManager = AudioManager()
    val countDownManager = CountdownManager(30000L, onFinish = ::nextTrack)

    var tracks: List<Track> = mutableListOf()

    var currentTrackIndex: Int = 0;
    var score = 0

    suspend fun loadTracks() {
        val url = URL(settings.playlistUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("Failed to load tracks")
        }

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val json = Gson().fromJson(response, JsonObject::class.java)
        val tracksJson = json["tracks"].asJsonObject["data"].toString()

        // Assurez-vous d'utiliser un TypeToken explicite pour une liste de Track
        tracks = Gson().fromJson(tracksJson, object : TypeToken<List<Track>>() {}.type)
    }


    fun nextTrack() {
        // Stop the current track
        audioManager.stop()

        // Play the next track
        if (currentTrackIndex >= tracks.size - 1) {
            return
        }

        currentTrackIndex++
        audioManager.play(getCurrentTrack().preview)

        // Restart the countdown
        countDownManager.restart()
    }

    fun getCurrentTrack(): Track {
        if (tracks.isEmpty()) {
            throw IllegalStateException("La liste des pistes est vide")
        }
        return tracks[currentTrackIndex]
    }


    fun start() {
        if (tracks.isEmpty()) {
            throw IllegalStateException("Aucune piste n'a été trouvée dans la playlist")
        }
        countDownManager.start()
        audioManager.play(tracks[currentTrackIndex].preview)

    }
}