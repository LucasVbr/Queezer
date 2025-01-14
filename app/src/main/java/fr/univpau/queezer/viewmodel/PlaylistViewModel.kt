package fr.univpau.queezer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.univpau.queezer.data.Input
import fr.univpau.queezer.data.Playlist
import fr.univpau.queezer.data.Track
import fr.univpau.queezer.service.PlaylistResponse
import fr.univpau.queezer.service.createDeezerApiService
import kotlinx.coroutines.launch

class PlaylistViewModel(private val url: String) : ViewModel() {

    var playlist: Playlist? = null

    init {
        val deezerApiService = createDeezerApiService()

        viewModelScope.launch {
            val playlistResponse : PlaylistResponse = deezerApiService.getPlaylist(url)

            playlist = Playlist(
                title = playlistResponse.title,
                tracks = playlistResponse.tracks.data.map { track ->
                    Track(
                        preview = track.preview,
                        album = track.album.cover,
                        title = Input(value = track.title),
                        artist = Input(value = track.artist.name)
                    )
                }.shuffled()
            )
        }
    }
}