package com.murerwa.moviesasa.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table")
data class Genre(
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)