package com.murerwa.moviesasa.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.room.dao.MovieDao
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.room.dao.GenreDao


@Database(entities = [Movie::class, Genre::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}