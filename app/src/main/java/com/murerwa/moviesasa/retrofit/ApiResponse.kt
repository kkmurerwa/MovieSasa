package com.murerwa.moviesasa.retrofit

import com.google.gson.annotations.SerializedName
import com.murerwa.moviesasa.models.Movie

data class ApiResponse(
    val page: Int,
    @SerializedName("results")
    val moviesLists: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)