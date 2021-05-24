package com.murerwa.moviesasa.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.murerwa.moviesasa.models.ApiKeys

@Dao
interface ApiKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveApiKeys(apiKey: ApiKeys)

    @Query("SELECT * FROM api_keys ORDER BY id DESC")
    suspend fun getApiKeys(): List<ApiKeys>

    @Query("DELETE FROM api_keys")
    suspend fun clearApiKeys()

}