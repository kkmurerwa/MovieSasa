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
    private var page = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>,
    ): MediatorResult {
        return try {

//            if (loadType == LoadType.REFRESH) {
//                appDatabase.apiKeysDao.clearApiKeys()
//                appDatabase.movieDao.clearMovies()
//            }

            val response = apiService.getMovies(
                page = page,
            ).body()
            val listing = response?.moviesLists
            val movies = listing?.map { it }
            if (movies != null) {
                val after = if (response.page == 500) null else page + 1

                page = after!!
                appDatabase.withTransaction {
                    appDatabase.apiKeysDao
                        .saveApiKeys(ApiKeys(
                            0,
                            after,
                            if (page == 1) 0 else page - 1)
                        )
                    appDatabase.movieDao.savePosts(movies)
                }
            }
            MediatorResult.Success(endOfPaginationReached = response?.page != 500)
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