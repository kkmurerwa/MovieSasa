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
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>,
    ): MediatorResult {
        return try {

            val loadKey = when(loadType){
                LoadType.REFRESH -> getApiKeys()
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND ->{
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    getApiKeys()
                }
            }

            val response = apiService.getMovies(
                page = loadKey?.after ?: 1,
            ).body()
            val listing = response?.moviesLists
            val movies = listing?.map { it }
            if (movies != null) {
                appDatabase.withTransaction {
                    appDatabase.apiKeysDao
                        .saveApiKeys(ApiKeys(
                            0,
                            response.page,
                            null
                        ))
                    appDatabase.movieDao.saveMovies(movies)
                }
            }
            MediatorResult.Success(endOfPaginationReached = response?.page == response?.total_pages)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getApiKeys(): ApiKeys? {
        return appDatabase.apiKeysDao.getApiKeys().firstOrNull() /*?: ApiKeys(0, 1, 0)*/
    }

}