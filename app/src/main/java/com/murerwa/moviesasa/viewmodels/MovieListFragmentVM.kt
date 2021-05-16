package com.murerwa.moviesasa.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.repositories.AppRepository
import kotlinx.coroutines.launch

class MovieListFragmentVM(application: Application) : AndroidViewModel(application) {
    private val _currentPage: MutableLiveData<Int> = MutableLiveData<Int>(1)

    fun getMovieList () : LiveData<List<Movie>> {
        return AppRepository(getApplication()).getAllMovies()
    }

    fun loadMoreMovies () = viewModelScope.launch {
        AppRepository(getApplication()).loadMoviesFromApi(2)
    }

    fun insertAllMovies(movieList: List<Movie>) {
        AppRepository(getApplication()).insertAllMovies(movieList)
    }
}