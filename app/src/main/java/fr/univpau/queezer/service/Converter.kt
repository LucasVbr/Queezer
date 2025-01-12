package fr.univpau.queezer.service

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.univpau.queezer.data.Answer
import fr.univpau.queezer.data.Playlist
import fr.univpau.queezer.data.Settings
import fr.univpau.queezer.data.Track
import java.util.Date

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromSettings(settings: Settings): String {
        return gson.toJson(settings)
    }

    @TypeConverter
    fun toSettings(data: String): Settings {
        return gson.fromJson(data, Settings::class.java)
    }

    @TypeConverter
    fun fromTrackList(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }

    @TypeConverter
    fun toTrackList(data: String): List<Track> {
        val listType = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromAnswer(answer: Answer): String {
        return answer.name
    }

    @TypeConverter
    fun toAnswer(data: String): Answer {
        return Answer.valueOf(data)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPlaylist(playlist: Playlist): String {
        return gson.toJson(playlist)
    }

    @TypeConverter
    fun toPlaylist(data: String): Playlist {
        return gson.fromJson(data, Playlist::class.java)
    }
}
