package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.murerwa.moviesasa.adapters.MovieAdapter
import com.murerwa.moviesasa.databinding.FragmentMovieListBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM

class MovieList : Fragment() {
    private var _binding: FragmentMovieListBinding? = null

    private val binding get() = _binding!!

    private lateinit var rvMovieList : RecyclerView

    private lateinit var viewModel: MovieListFragmentVM

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)

        movieAdapter = MovieAdapter{ movie: Movie -> navigateToSingleView(movie)}

        val toolbar = binding.mainToolbar.root
        toolbar.title = "Home"

        rvMovieList = binding.rvAllShows
        rvMovieList.layoutManager = LinearLayoutManager(context)

//        val movieList: MutableList<Movie> = ArrayList()
//
//        for (i: Int in 0..5) {
//            val movie = Movie(
//                title = "Movie $i",
//                description = "Desc $i",
//                coverUrl = "https://1.bp.blogspot.com/-LtwS1ngzciI/W-X54-7ufkI/AAAAAAAAEDw/YClythlEPW8t-qTvgkGZihWIzIn3yUVhACK4BGAYYCw/s1600/universalpictures.jpg",
//                rating = i
//            )
//
//            movieList.add(movie)
//        }
//
//        ioThread {
//            viewModel.insertAllMovies(movieList)
//        }

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

    private fun navigateToSingleView (movie: Movie) {
        // Navigate with safe-args
        val action = MovieListDirections.actionMovieListFragmentToMovieViewFragment(movie)

        // Perform navigation action
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}