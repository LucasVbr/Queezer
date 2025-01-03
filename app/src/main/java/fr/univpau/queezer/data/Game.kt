package fr.univpau.queezer.data

import java.sql.Date

data class Game(
    val settings: Settings,
    val tracks: List<Track>,
    val score: Int,
    val date: Date
)
