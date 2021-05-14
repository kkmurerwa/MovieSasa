package com.murerwa.moviesasa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.repositories.AppRepository

class MovieListFragmentVM(application: Application) : AndroidViewModel(application) {
    private val _movieList = MutableLiveData<List<Movie>>()

    fun getMovieList () : LiveData<List<Movie>> {
        return AppRepository(getApplication()).getAllMovies()
    }

    fun insertAllMovies(movieList: List<Movie>) {
        AppRepository(getApplication()).insertAllMovies(movieList)
    }
}