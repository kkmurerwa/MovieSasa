package com.murerwa.moviesasa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.ListItemMovieBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.utils.Config
import com.murerwa.moviesasa.utils.DiffUtilCallBack

class MovieListAdapter(private val movieCardClicked: (Movie) -> Unit) :
    PagingDataAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(view, movieCardClicked)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bindPost(movie)
        }
    }

    class MovieViewHolder(itemView: View, private val movieItemClicked: (Movie) -> Unit)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val binding: ListItemMovieBinding = ListItemMovieBinding.bind(itemView)

        private lateinit var currentMovie: Movie

        override fun onClick(v: View?) {
            // navigate to next item when clicked
            movieItemClicked.invoke(currentMovie)
        }

        fun bindPost(movie: Movie) {
            this.currentMovie = movie

            binding.root.setOnClickListener(this)

            with(movie) {
                binding.tvMovieTitle.text = title
                binding.tvMovieDesc.text = overview
                binding.rbMovieRating.rating = Config().ratingConverter(vote_average)

                // Create spinner drawable for the glide placeholder
                val progressDrawable = CircularProgressDrawable(binding.root.context)
                progressDrawable.strokeWidth = 5f
                progressDrawable.centerRadius = 30f
                progressDrawable.start()

                // Load images with glide
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w500/${poster_path}")
                    .placeholder(progressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.ic_no_movie)
                    .fallback(R.drawable.ic_no_movie)
                    .into(binding.imvMovieCover)
            }
        }
    }
}