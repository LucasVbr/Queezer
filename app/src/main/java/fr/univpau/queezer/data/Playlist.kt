package fr.univpau.queezer.data

data class Playlist(
    val title: String = "",
    val tracks: List<Track> = emptyList()
)
