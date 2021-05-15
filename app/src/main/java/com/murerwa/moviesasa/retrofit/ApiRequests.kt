package com.murerwa.moviesasa.retrofit

import retrofit2.Call
import retrofit2.http.GET

const val API_KEY = "eb16e8a46409e5e4644e1f1a6b7be4f7"

interface ApiRequests {
    @GET("/3/movie/popular?api_key=$API_KEY&language=en-US&page=1")
    fun getFeaturedMovies(): Call<ApiResponse>

}