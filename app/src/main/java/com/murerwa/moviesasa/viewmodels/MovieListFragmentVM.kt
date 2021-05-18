package com.murerwa.moviesasa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.repositories.AppRepository
import kotlinx.coroutines.flow.Flow

class MovieListFragmentVM(application: Application) : AndroidViewModel(application) {
    private val movieRepo = AppRepository(application)

    fun fetchPosts(): Flow<PagingData<Movie>> {
        return movieRepo.getMovies().cachedIn(viewModelScope)
    }
}