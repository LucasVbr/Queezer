package fr.univpau.queezer.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.univpau.queezer.data.GameDao
import fr.univpau.queezer.data.Game
import kotlinx.coroutines.launch

class GameViewModel(private val gameDao: GameDao) : ViewModel() {

    val games: LiveData<List<Game>> = gameDao.getAll()

    fun addGame(context: Context, game: Game) {
        viewModelScope.launch {
            try {
                game.settings.validate(context)
                gameDao.insert(game)
            } catch (e: IllegalArgumentException) {
                Log.e("GameViewModel", "Error adding game: ${e.message}")
            }
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch {
            gameDao.delete(game)
        }
    }
}
