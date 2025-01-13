package fr.univpau.queezer.data

data class Filter(
    val date: DateFilter = DateFilter.DESCENDING,
    val mode : List<GameMode> = listOf(GameMode.TITLE, GameMode.ARTIST, GameMode.ALL),
    val nbTitle : Int? = null,
)

fun filterGames(filter: Filter, games: List<Game>): List<Game> {
    return games.filter { game ->
        filter.mode.contains(game.settings.gameMode) && (filter.nbTitle == null || game.playlist.tracks.size == filter.nbTitle)
    }.sortedBy { game ->
        when (filter.date) {
            DateFilter.ASCENDING -> game.date.time
            DateFilter.DESCENDING -> -game.date.time
        }
    }
}

enum class DateFilter {
    ASCENDING,
    DESCENDING
}