package fr.univpau.queezer.data

import android.util.Log

data class Filter(
    var dateOrderIsAscending: Boolean = true,

    val mode : Map<GameMode, Boolean> = mapOf(
        GameMode.TITLE to true,
        GameMode.ARTIST to true,
        GameMode.ALL to true
    ),

    val nbTitleIsAscending : Boolean = true,
)

fun filterGames(filter: Filter, games: List<Game>): List<Game> {
    Log.i("Filter", "Filtering games with $filter")

    return games
        .filter { game ->
            // Filtrer uniquement par les modes de jeu activés
            filter.mode.filter { it.value }.keys.contains(game.settings.gameMode)
        }
        .sortedWith(compareBy<Game> { game ->
            // Tri par date
            if (filter.dateOrderIsAscending) -game.date.time else game.date.time
        }.thenBy { game ->
            // Tri par nombre de titres
            if (filter.nbTitleIsAscending) game.settings.numberOfTitles ?: 0 else -(game.settings.numberOfTitles ?: 0)
        })
}