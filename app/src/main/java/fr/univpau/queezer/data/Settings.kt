package fr.univpau.queezer.data

import fr.univpau.queezer.R

data class Settings(
    var gameMode: GameMode = GameMode.TITLE,
    var numberOfTitles: Int? = 5,
    var playlistUrl: String = "https://api.deezer.com/playlist/13279914183",
) {
    fun validate(context: android.content.Context) {
        if (playlistUrl.isEmpty()) {
            throw IllegalArgumentException(context.resources.getString(R.string.error_playlist_url_empty))
        }

        if (!playlistUrl.startsWith("https://api.deezer.com/playlist/")) {
            throw IllegalArgumentException(context.resources.getString(R.string.error_playlist_url_invalid))
        }

        if (numberOfTitles == null) {
            throw IllegalArgumentException(context.resources.getString(R.string.error_tracks_count_empty))
        }

        if (numberOfTitles!! <= 0) {
            throw IllegalArgumentException(context.resources.getString(R.string.error_tracks_count_negative))
        }
    }
}