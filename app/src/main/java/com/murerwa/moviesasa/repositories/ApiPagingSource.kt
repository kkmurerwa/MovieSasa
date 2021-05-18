package com.murerwa.moviesasa.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.murerwa.moviesasa.models.ApiKeys
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiService
import com.murerwa.moviesasa.room.db.AppDatabase
import retrofit2.HttpException
import java.io.IOException

class ApiPagingSource(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
    ) : PagingSource<Int, Movie>() {

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, Movie> {

        val page = params.key ?: 1

        return try {
            val movies: List<Movie>?

            val response = apiService.getMovies(page = page).body()
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (response?.page == 500) null else page + 1

            movies = response?.moviesLists

            if (movies?.size!! > 0) {
                appDatabase.withTransaction {
                    appDatabase.movieDao.saveMovies(movies)
                }
            }

            LoadResult.Page(
                movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) { // 6
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        TODO("Not yet implemented")
    }
}