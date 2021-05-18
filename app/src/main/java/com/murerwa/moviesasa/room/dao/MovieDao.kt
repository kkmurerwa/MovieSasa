package com.murerwa.moviesasa.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.murerwa.moviesasa.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = REPLACE)
    suspend fun savePosts(movies: List<Movie>)

    @Insert(onConflict = REPLACE)
    suspend fun saveMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies_table")
    fun getPosts(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movies_table")
    fun getMovies(): List<Movie>

    @Query("SELECT COUNT(*) FROM movies_table")
    fun getDbCount(): Int

    @Insert
    fun insertMovie(movie: Movie)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movies_table")
    fun clearMovies()
}
