package com.murerwa.moviesasa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.repositories.AppRepository

class SingleMovieFragmentVM(application: Application) : AndroidViewModel(application) {
    fun getGenreList () : LiveData<List<Genre>> {
        return AppRepository(getApplication()).getAllMovieGenres()
    }

    fun getGenre(id: Int) : LiveData<Genre> {
        return AppRepository(getApplication()).getGenre(id)
    }

    fun insertAllGenres(genreList: List<Genre>) {
        AppRepository(getApplication()).insertAllGenres(genreList)
    }
}