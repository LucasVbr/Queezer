package fr.univpau.queezer.manager

import android.content.Context
import com.google.gson.Gson
import fr.univpau.queezer.data.Settings

fun saveSettings(context: Context, settings: Settings) {

    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Sérialiser l'objet Settings en JSON
    val json = Gson().toJson(settings)

    // Sauvegarder le JSON dans les SharedPreferences
    editor.putString("settings", json)
    editor.apply()
}

fun loadSettings(context: Context): Settings {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Récupérer le JSON depuis SharedPreferences
    val json = sharedPreferences.getString("settings", null)

    // Si le JSON n'est pas null, le convertir en objet Settings
    return if (json != null) {
        Gson().fromJson(json, Settings::class.java)
    } else Settings()
}
