package fr.univpau.queezer

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

data class Track(
    val title: String,
    val artist: Artist,
    val preview: String,
    val album: Album
)

data class Artist(
    val name: String
)

data class Album(
    val cover: String
)


suspend fun fetchTracks(apiUrl: String): List<Track> {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = Gson().fromJson(response, JsonObject::class.java)
                val tracksJson = json["tracks"].asJsonObject["data"].toString()
                val trackListType = object : TypeToken<List<Track>>() {}.type
                Gson().fromJson<List<Track>>(tracksJson, trackListType)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

suspend fun displayTracks(apiUrl: String) {
    val tracks = fetchTracks(apiUrl)

    tracks?.forEach { track ->
        Log.d("Track", track.title)
        Log.d("Artist", track.artist.name)
        Log.d("Preview", track.preview)
        Log.d("Album", track.album.cover)
    }
}
