package com.murerwa.moviesasa.repositories

import androidx.paging.PagingSource
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

abstract class ApiPagingSource(private val apiService: ApiService) :
    PagingSource<String, Movie>() {

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<String>):
            LoadResult<String, Movie> {
        return try {
            // 1
            val response = apiService.fetchPosts(/*page = params.loadSize*/)
            val moviePosts = response.body()?.moviesLists
            // 3
            LoadResult.Page(
                moviePosts ?: listOf(),
                "0",
                "1"
            )
        } catch (exception: IOException) { // 6
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}