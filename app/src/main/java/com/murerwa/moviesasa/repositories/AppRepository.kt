package com.murerwa.moviesasa.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiRequests
import com.murerwa.moviesasa.retrofit.ApiResponse
import com.murerwa.moviesasa.room.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org"

class AppRepository(context: Context) {
    private val db: AppDatabase = AppDatabase.getInstance(context)

    fun getAllMovies(): LiveData<List<Movie>> {
        // Check if db is empty on background thread
        GlobalScope.launch(Dispatchers.IO) {
            if (db.movieDao.getDbCount() == 0) {
                loadMoviesFromApi()
            }
        }

        return db.movieDao.getAllDbMovies()
    }

    fun insertAllMovies(movieList: List<Movie>) {
        movieList.forEach {
            db.movieDao.insertMovie(it)
        }
    }

    private fun loadMoviesFromApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val api: ApiRequests? = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            val response: Response<ApiResponse> = api!!.getFeaturedMovies().awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d("DATA", data.toString())

                insertAllMovies(data.moviesLists)

//                withContext(Dispatchers.Main) {
//                    return@withContext db.movieDao.getAllDbMovies()
//                }
            }
        }
    }
}