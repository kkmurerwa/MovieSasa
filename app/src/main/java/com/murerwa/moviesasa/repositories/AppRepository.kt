package com.murerwa.moviesasa.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.murerwa.moviesasa.models.Cast
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiRequests
import com.murerwa.moviesasa.retrofit.CastApiResponse
import com.murerwa.moviesasa.retrofit.GenreListResponse
import com.murerwa.moviesasa.retrofit.MoviesApiResponse
import com.murerwa.moviesasa.room.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org"

class AppRepository(context: Context) {
    private val db: AppDatabase = AppDatabase.getInstance(context)

    private val _movieList:MutableLiveData<List<Cast>> = MutableLiveData<List<Cast>>()

    fun getAllMovies(): LiveData<List<Movie>> {
        // Check if db is empty on background thread
        GlobalScope.launch(Dispatchers.IO) {
            if (db.movieDao.getDbCount() == 0) {
                loadMoviesFromApi()
            }
        }

        return db.movieDao.getAllDbMovies()
    }

    fun getAllMovieGenres(): LiveData<List<Genre>> {
        // Check if db is empty on background thread
        GlobalScope.launch(Dispatchers.IO) {
            if (db.genreDao.getDbCount() == 0) {
                loadGenresFromApi()
            }
        }

        return db.genreDao.getAllMovieGenres()
    }

    fun loadMoviesFromApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api: ApiRequests? = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            val responseMovies: Response<MoviesApiResponse> = api!!.getFeaturedMovies().awaitResponse()

            if (responseMovies.isSuccessful) {
                val data = responseMovies.body()!!
                Log.d("DATA", data.toString())

                insertAllMovies(data.moviesLists)
            }
        }
    }

    fun insertAllMovies(movieList: List<Movie>) {
        movieList.forEach {
            db.movieDao.insertMovie(it)
        }
    }

    fun getGenre(id: Int): LiveData<Genre> {
        return db.genreDao.getGenre(id)
    }

    fun insertAllGenres(genreList: List<Genre>) {
        genreList.forEach {
            db.genreDao.insertGenre(it)
        }
    }

    private fun loadGenresFromApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api: ApiRequests? = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            val response: Response<GenreListResponse> = api!!.getAllGenres().awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d("DATA", data.toString())

                insertAllGenres(data.genres)
            }
        }
    }

    fun getFilmCast(filmId: String): MutableLiveData<List<Cast>> {
        loadFilmCastFromApi(filmId)

        return _movieList
    }

    private fun loadFilmCastFromApi(filmId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val api: ApiRequests? = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            val response: Response<CastApiResponse> = api!!.getFilmCast(filmId).awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()!!

                _movieList.postValue(data.cast)
            }
        }
    }
}