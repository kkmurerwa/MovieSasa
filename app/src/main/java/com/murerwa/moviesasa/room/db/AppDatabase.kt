package com.murerwa.moviesasa.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.models.ApiKeys
import com.murerwa.moviesasa.repositories.AppRepository
import com.murerwa.moviesasa.room.dao.ApiKeysDao
import com.murerwa.moviesasa.room.dao.GenreDao
import com.murerwa.moviesasa.room.dao.MovieDao


@Database(
    entities = [Movie::class, Genre::class, ApiKeys::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao
    abstract val apiKeysDao: ApiKeysDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {

                return if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    instance!!
                } else {
                    instance!!
                }

            }
        }
    }
}