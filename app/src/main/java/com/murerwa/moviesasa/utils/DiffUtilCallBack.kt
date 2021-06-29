package com.murerwa.moviesasa.utils

import androidx.recyclerview.widget.DiffUtil
import com.murerwa.moviesasa.models.Movie

class DiffUtilCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return true
    }
}