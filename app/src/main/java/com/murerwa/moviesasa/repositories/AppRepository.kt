package com.murerwa.moviesasa.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.murerwa.moviesasa.room.db.AppDatabase
import com.murerwa.moviesasa.models.Movie

class AppRepository(private val context: Context) {
    private val db: AppDatabase = AppDatabase.getInstance(context)
    fun getAllMovies(): LiveData<List<Movie>> {
        return db.movieDao.getAllDbMovies()
    }

    fun insertAllMovies(movieList: List<Movie>) {
        movieList.forEach {
            db.movieDao.insertMovie(it)
        }
    }
}