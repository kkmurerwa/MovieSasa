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
import com.murerwa.moviesasa.utils.toast

class MovieAdapter(private val movieCardClicked: (Movie) -> Unit)
        : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val movieList: ArrayList<Movie> = arrayListOf()

    private lateinit var context: Context
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
        return ViewHolder(inflatedView,  movieCardClicked)
    }

    override fun getItemCount() = movieList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.bindMovie(currentMovie, position, movieList)
    }

    class ViewHolder(v: View, private val movieItemClicked: (Movie) -> Unit)
        : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        private lateinit var currentMovie: Movie

        private var _binding: ListItemMovieBinding? = null

        private val binding get() = _binding!!

        init {
            v.setOnClickListener(this)
            _binding = ListItemMovieBinding.bind(view)
        }

        override fun onClick(v: View) {
            // navigate to next item when clicked
            movieItemClicked.invoke(currentMovie)
        }

        fun bindMovie(movieItem: Movie, position: Int, movieList: List<Movie>) {
            this.currentMovie = movieItem

            binding.tvMovieTitle.text = movieItem.title
            binding.tvMovieDesc.text = movieItem.overview
            binding.tvMovieAddInfo.text = movieItem.vote_average.toString()

            val progressDrawable = CircularProgressDrawable(binding.root.context)
            progressDrawable.strokeWidth = 5f
            progressDrawable.centerRadius = 30f
            progressDrawable.start()

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500/${movieItem.poster_path}")
                .placeholder(progressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_no_movie)
                .fallback(R.drawable.ic_no_movie)
                .into(binding.imvMovieCover)
        }
    }
}