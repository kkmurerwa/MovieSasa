package com.murerwa.moviesasa.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.murerwa.moviesasa.models.Cast
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.*
import com.murerwa.moviesasa.room.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org"

class AppRepository(context: Context) {
    private val apiService = ApiClient.getClient().create(ApiService::class.java)

    private val appDatabase = AppDatabase.getInstance(context)

    private val db: AppDatabase = AppDatabase.getInstance(context)

    private val _movieList: MutableLiveData<List<Cast>> = MutableLiveData<List<Cast>>()


    @OptIn(ExperimentalPagingApi::class)
    fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = ApiRemoteMediator(apiService, appDatabase),
            pagingSourceFactory = { appDatabase.movieDao.getMovies() }
        ).flow
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

//    private suspend fun insertAllGenres(genreList: List<Genre>) {
//        db.genreDao.insertGenres(genreList)
////        genreList.forEach {
////
////        }
//    }

    private fun loadGenresFromApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api: ApiRequests? = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            try {
                val response: Response<GenreListResponse> = api!!.getAllGenres().awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d("DATA", data.toString())

                    db.genreDao.insertGenres(data.genres)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
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

            try {
                val response: Response<CastApiResponse> = api!!.getFilmCast(filmId).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!

                    _movieList.postValue(data.cast)
                }
            } catch (exception: Exception) {
                _movieList.postValue(null)
            }
        }
    }

}