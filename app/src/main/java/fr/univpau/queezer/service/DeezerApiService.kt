package fr.univpau.queezer.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

data class PlaylistResponse(
    val tracks: TrackList
)

data class TrackList(
    val data: List<ApiTrack>
)

data class ApiTrack(
    val preview: String,  // URL d'aperçu de la musique
    val album: ApiAlbum,  // Informations sur l'album
    val title: String,    // Titre de la piste
    val artist: Artist    // Informations sur l'artiste
)

data class ApiAlbum(
    val cover: String     // URL de la couverture de l'album
)

data class Artist(
    val name: String      // Nom de l'artiste
)


interface DeezerApiService {
    @GET
    suspend fun getPlaylist(@Url url: String): PlaylistResponse
}

fun createDeezerApiService(): DeezerApiService {
    return Retrofit.Builder()
        .baseUrl("https://api.deezer.com/") // Base URL par défaut
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeezerApiService::class.java)
}