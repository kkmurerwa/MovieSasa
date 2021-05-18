package com.murerwa.moviesasa.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_keys")
data class ApiKeys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val after: Int?,
    val before: Int?
)
