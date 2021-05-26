package com.murerwa.moviesasa.repositories

import androidx.paging.*
import androidx.room.withTransaction
import com.murerwa.moviesasa.models.ApiKeys
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiService
import com.murerwa.moviesasa.room.db.AppDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ApiRemoteMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Movie>() {
    private val STARTING_PAGE_INDEX: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>,
    ): MediatorResult {
        return try {

            val loadKey = when(loadType){
                LoadType.REFRESH -> return MediatorResult.Success(false)
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND ->{
                    getApiKeys()
                }
            }

            val page: Int = loadKey?.after ?: STARTING_PAGE_INDEX

            val response = apiService.getMovies(
                page = page,
            ).body()
            val listing = response?.moviesLists
            val movies = listing?.map { it }

            val endOfPaginationReached = loadKey?.after == null || response?.page == response?.total_pages

            if (movies != null) {
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                appDatabase.withTransaction {
                    appDatabase.apiKeysDao
                        .saveApiKeys(ApiKeys(
                            1,
                            nextKey,
                            prevKey
                        ))
                    appDatabase.movieDao.saveMovies(movies)
                }
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getApiKeys(): ApiKeys? {
        return appDatabase.apiKeysDao.getApiKeys().firstOrNull()
    }

}