package fr.univpau.queezer

import android.content.Context
import android.content.SharedPreferences

val gameModes = listOf("Titre Uniquement", "Artiste Uniquement", "Titre et Artiste")

fun saveSettings(context: Context, gameMode: String, numberOfTitles: String, playlistUrl: String) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("gameMode", gameMode)
        putString("numberOfTitles", numberOfTitles)
        putString("playlistUrl", playlistUrl)
        apply()
    }
}

fun loadSettings(context: Context): Settings {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val gameMode = sharedPreferences.getString("gameMode", gameModes[0]) ?: gameModes[0]
    val numberOfTitles = sharedPreferences.getString("numberOfTitles", "30") ?: "30"
    val playlistUrl = sharedPreferences.getString("playlistUrl", "") ?: ""
    return Settings(gameMode, numberOfTitles, playlistUrl)
}

data class Settings(val gameMode: String, val numberOfTitles: String, val playlistUrl: String)
