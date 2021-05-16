package com.murerwa.moviesasa.utils

class Config {
    fun ratingConverter (rating: Double): Float {
        return ((rating/10) * 5).toFloat()
    }
}