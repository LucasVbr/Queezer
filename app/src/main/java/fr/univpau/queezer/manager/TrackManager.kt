package fr.univpau.queezer.manager

import fr.univpau.queezer.data.Input
import fr.univpau.queezer.data.Track
import fr.univpau.queezer.service.createDeezerApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fetchAndFormatPlaylist(apiUrl: String): List<Track> {
    val deezerApiService = createDeezerApiService()

    return withContext(Dispatchers.IO) {
        try {
            // Récupérer la réponse de la playlist
            val playlist = deezerApiService.getPlaylist(apiUrl)

            // Transformer les données en liste de `Track`
            playlist.tracks.data.map { track ->
                Track(
                    preview = track.preview,
                    album = track.album.cover,
                    title = Input(value = track.title),
                    artist = Input(value = track.artist.name)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}