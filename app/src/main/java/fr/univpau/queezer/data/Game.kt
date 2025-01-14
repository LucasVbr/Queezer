package fr.univpau.queezer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val settings: Settings = Settings(),
    val playlist: Playlist = Playlist(),
    val score: Int = 0,
    val date: Date = Date()
)