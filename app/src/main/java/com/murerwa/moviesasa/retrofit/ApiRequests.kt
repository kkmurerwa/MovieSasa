package com.murerwa.moviesasa.retrofit

import com.murerwa.moviesasa.BuildConfig
import retrofit2.Call
import retrofit2.http.GET


const val BASE_URL = "https://api.themoviedb.org"

interface ApiRequests {
    @GET("/3/movie/popular?api_key=${BuildConfig.API_KEY}&language=en-US&page=1")
    fun getFeaturedMovies(): Call<ApiResponse>

}