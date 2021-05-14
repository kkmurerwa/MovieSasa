package com.murerwa.moviesasa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.ListItemMovieBinding
import com.murerwa.moviesasa.models.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val movieList: ArrayList<Movie> = arrayListOf()

    private lateinit var context: Context


    // Setting new list, avoid passing list as constructor
    fun setList(newList: ArrayList<Movie>) {
        movieList.clear()
        movieList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = movieList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Movie = movieList[position]
        holder.bindMovie(Movie, position, movieList, context)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        private var _binding: ListItemMovieBinding? = null

        private val binding get() = _binding!!

        init {
            v.setOnClickListener(this)
            _binding = ListItemMovieBinding.bind(view)
        }

        override fun onClick(v: View) {
            // Do stuff when card clicked
        }

        fun bindMovie(movieItem: Movie, position: Int, movieList: List<Movie>, context: Context) {
            binding.tvMovieTitle.text = movieItem.title
            binding.tvMovieDesc.text = movieItem.description
            binding.tvMovieAddInfo.text = movieItem.rating.toString()

            val progressDrawable = CircularProgressDrawable(context)
            progressDrawable.strokeWidth = 5f
            progressDrawable.centerRadius = 30f
            progressDrawable.start()

            Glide.with(itemView)
                .load(movieItem.coverUrl)
                .centerCrop()
                .placeholder(progressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_no_movie)
                .fallback(R.drawable.ic_no_movie)
                .into(binding.imvMovieCover)
        }
    }
}