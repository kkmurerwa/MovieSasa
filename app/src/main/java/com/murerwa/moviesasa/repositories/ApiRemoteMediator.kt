package com.murerwa.moviesasa.repositories

import androidx.paging.*
import androidx.room.withTransaction
import com.murerwa.moviesasa.models.ApiKeys
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.retrofit.ApiService
import com.murerwa.moviesasa.room.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class ApiRemoteMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Movie>() {
    private val STARTING_PAGE_INDEX: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>,
    ): MediatorResult {
        val apiKeysDao = appDatabase.apiKeysDao

//        val loadKey = when (loadType) {
//            LoadType.REFRESH -> {
//                withContext(Dispatchers.IO) {
//                    when (appDatabase.movieDao.getDbCount() > 0) {
//                        true -> return@withContext MediatorResult.Success(false)
//                        else -> {}
//                    }
//                }
//                null
//            }
//            LoadType.PREPEND -> return MediatorResult.Success(true)
//            LoadType.APPEND -> {
//                getApiKeys()
//            }
//        }

        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = apiKeysDao.getApiKeys().lastOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem
                }
            }

            if (loadKey != null) {
                if (loadKey.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = loadKey?.after ?: STARTING_PAGE_INDEX

            val response = apiService.getMovies(
                page = page,
            )
            val movies = response.moviesLists

            //500 is the total page in this specific api, not a must , the second comparator can still work
            val endOfPaginationReached = response.page >= response.total_pages

            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1

            appDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    apiKeysDao.clearApiKeys()
                }

                appDatabase.apiKeysDao
                    .saveApiKeys(
                        ApiKeys(
                            1,
                            after = nextKey,
                            before = prevKey,
                            isEndReached = endOfPaginationReached
                        )
                    )
                appDatabase.movieDao.saveMovies(movies)

            }
            return MediatorResult.Success(endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH

//        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)

//        return if (System.currentTimeMillis() - db.lastUpdated() >= cacheTimeout) {
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
//    }

    private suspend fun getApiKeys(): ApiKeys? {
        return appDatabase.apiKeysDao.getApiKeys().firstOrNull()
    }

}