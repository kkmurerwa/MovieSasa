package com.murerwa.moviesasa.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GenreIdsConverter {
    private val gson = Gson()

    @TypeConverter
    fun from(genreIdList: List<Int>?) : String? {
        if (genreIdList == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Int>?>() {}.type
        return gson.toJson(genreIdList, type)
    }

    @TypeConverter
    fun to(genreIdListString: String?) : List<Int>? {
        if (genreIdListString == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Int>?>() {}.type
        return gson.fromJson(genreIdListString, type)
    }

}