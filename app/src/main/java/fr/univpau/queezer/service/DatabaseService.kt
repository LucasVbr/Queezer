package fr.univpau.queezer.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.univpau.queezer.data.GameDao
import fr.univpau.queezer.data.Game

@Database(entities = [Game::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseService : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseService? = null

        fun getDatabase(context: Context): DatabaseService {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseService::class.java,
                    "app_notes_database"
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}