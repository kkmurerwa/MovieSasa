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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}