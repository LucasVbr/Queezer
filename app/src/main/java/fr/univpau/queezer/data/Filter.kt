package fr.univpau.queezer.data

import android.util.Log

data class Filter(
    // var dateOrderIsAscending: Boolean = true,
    val orderByNbTitle : Boolean = false,

    val mode : Map<GameMode, Boolean> = mapOf(
        GameMode.TITLE to true,
        GameMode.ARTIST to true,
        GameMode.ALL to true
    ),

)

fun filterGames(filter: Filter, games: List<Game>): List<Game> {
    Log.i("Filter", "Filtering games with $filter")

    return games
        .filter { game ->
            filter.mode.filter { it.value }.keys.contains(game.settings.gameMode)
        }
        .sortedWith(compareBy<Game> { game ->
            // Tri par date
            if (filter.orderByNbTitle) {
                -game.settings.numberOfTitles!!
            } else {
                -game.date.time
            }
        })
}