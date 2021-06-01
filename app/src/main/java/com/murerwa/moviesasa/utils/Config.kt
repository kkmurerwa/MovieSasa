package com.murerwa.moviesasa.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class Config {
    fun ratingConverter (rating: Double): Float {
        return ((rating/10) * 5).toFloat()
    }

    fun hasNetwork(context: Context): Boolean? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }
}