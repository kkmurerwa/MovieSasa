package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.murerwa.moviesasa.utils.ioThread
import com.murerwa.moviesasa.room.db.AppDatabase
import com.murerwa.moviesasa.adapters.MovieAdapter
import com.murerwa.moviesasa.databinding.FragmentFirstBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM

class MovieList : Fragment() {
    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private lateinit var rvMovieList : RecyclerView

    private lateinit var viewModel: MovieListFragmentVM

    private val movieAdapter = MovieAdapter()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)

        val toolbar = binding.mainToolbar.root
        toolbar.title = "Home"

        rvMovieList = binding.rvAllShows
        rvMovieList.layoutManager = LinearLayoutManager(context)

//        val movieList: MutableList<Movie> = ArrayList()
//        for (i in 0..10) {
//            val movie = Movie(
//                title = "Title $i",
//                description = "Desc $i",
//                coverUrl = "https://i.pinimg.com/originals/1a/4d/23/1a4d2318d4a3186e0e1b7659355f88f2.jpg",
//                rating = i
//            )
//
//            Log.d("Array Size", movie.title)
//
//            movieList.add(movie)
//        }

//        ioThread {
//            val viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)
//
//            viewModel.insertAllMovies(movieList)
//        }

//        Log.d("Array Size", movieList.size.toString())

        observeDb()
    }

    private fun observeDb() {
        // Use an observer to monitor the db and reflect changes on adapter from the view model
        viewModel.getMovieList().observe(viewLifecycleOwner, { movieList ->
            movieAdapter.setList(movieList as ArrayList<Movie>)
            context?.let {context -> movieAdapter.setContext(context) }

            rvMovieList.adapter = movieAdapter
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}