package com.murerwa.moviesasa.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table")
data class Movie(
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "movie_title")
    val title: String,
    @ColumnInfo(name = "movie_desc")
    val description: String,
    @ColumnInfo(name = "movie_url")
    val coverUrl: String,
    @ColumnInfo(name = "movie_rating")
    val rating: Int,
)