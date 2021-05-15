package com.murerwa.moviesasa

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.murerwa.moviesasa.databinding.ActivityMainBinding
import com.murerwa.moviesasa.retrofit.ApiResponse
import com.murerwa.moviesasa.retrofit.ApiRequests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getCurrentData()
    }

    private fun getCurrentData() {
        val api: ApiRequests? = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response: Response<ApiResponse> = api!!.getFeaturedMovies().awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d("DATA", data.toString())
            }

        }
    }
}