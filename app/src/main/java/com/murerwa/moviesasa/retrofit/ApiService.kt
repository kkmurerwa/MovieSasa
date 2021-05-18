package com.murerwa.moviesasa.retrofit

import com.murerwa.moviesasa.BuildConfig
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/3/movie/popular?")
    suspend fun getMovies(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("page") page: Int,
    ): Response<MoviesApiResponse>

    @GET("/3/search/movie?")
    suspend fun searchMovie(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("query") query: String = "",
        @Query("include_adult") include_adult: Boolean = false,
    ): Response<MoviesApiResponse>
}