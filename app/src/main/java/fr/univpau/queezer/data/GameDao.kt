package fr.univpau.queezer.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameDao {

    @Insert
    fun insert(game: Game)

    @Query("SELECT * FROM game")
    fun getAll(): LiveData<List<Game>>

    @Delete
    fun delete(game: Game)
}