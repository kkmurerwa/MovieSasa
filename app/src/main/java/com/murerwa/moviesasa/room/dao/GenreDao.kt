package com.murerwa.moviesasa.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.murerwa.moviesasa.models.Genre
import com.murerwa.moviesasa.models.Movie

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres_table ORDER BY id ASC")
    fun getAllMovieGenres(): LiveData<List<Genre>>

    @Query("SELECT * FROM genres_table WHERE id = :passedId")
    fun getGenre(passedId: Int): LiveData<Genre>

    @Query("SELECT COUNT(*) FROM genres_table")
    fun getDbCount(): Int

//    @Insert
//    fun insertGenre(genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(genre: Genre)

    @Delete
    fun deleteMovie(genre: Genre)
}