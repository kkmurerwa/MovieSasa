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
    private val apiService = ApiClient.getClient(context).create(ApiService::class.java)

    val TAG = "APP REPOSITORY"

    private val appDatabase = AppDatabase.getInstance(context)

//    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private val _movieList: MutableLiveData<List<Cast>> = MutableLiveData<List<Cast>>()

    //    private val pagingSource = { appDatabase.movieDao.getMovies() }
    private val pagingSource = { ApiPagingSource(apiService, appDatabase) }

    fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
//            remoteMediator = ApiRemoteMediator(apiService, appDatabase),
            pagingSourceFactory = pagingSource
        ).flow
    }

    fun getAllMovieGenres(): LiveData<List<Genre>> {
        // Check if db is empty on background thread
        GlobalScope.launch(Dispatchers.IO) {
            if (appDatabase.genreDao.getDbCount() == 0) {
                loadGenresFromApi()
            }
        }

        return appDatabase.genreDao.getAllMovieGenres()
    }

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

                    appDatabase.genreDao.insertGenres(data.genres)
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
                val response: Response<CastApiResponse> = api!!.getFilmCast(id = filmId).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!

                    _movieList.postValue(data.cast)

                    Log.d(TAG, "Successfully loaded cast")
                } else {
                    _movieList.postValue(null)
                    Log.d(TAG, "API did not return any results")
                }
            } catch (exception: Exception) {
                _movieList.postValue(null)
                Log.d(TAG, "Could not load cast")
            }
        }
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(context: Context) = instance ?:
        synchronized(this) {
            instance ?: AppRepository(context).also { instance = it }
        }
    }

}