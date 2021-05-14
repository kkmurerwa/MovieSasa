package com.murerwa.moviesasa.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.murerwa.moviesasa.models.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies_table ORDER BY id ASC")
    fun getAllDbMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies_table WHERE id = :passedId")
    fun getMovie(passedId: Int): Movie

    @Insert
    fun insertMovie(movie: Movie)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)
}