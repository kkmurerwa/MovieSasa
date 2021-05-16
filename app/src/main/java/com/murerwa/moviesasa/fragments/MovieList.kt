package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.murerwa.moviesasa.adapters.CastListAdapter
import com.murerwa.moviesasa.adapters.MovieAdapter
import com.murerwa.moviesasa.databinding.FragmentMovieListBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.utils.hideSoftKeyboard
import com.murerwa.moviesasa.utils.toast
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM
import kotlinx.coroutines.launch

class MovieList : Fragment() {
    private var _binding: FragmentMovieListBinding? = null

    private val binding get() = _binding!!

    private lateinit var rvMovieList : RecyclerView

    private lateinit var viewModel: MovieListFragmentVM

    private lateinit var movieAdapter: MovieAdapter

    private var _isLoadingMovies = false

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

        rvMovieList = binding.rvAllShows
        val layoutManager = LinearLayoutManager(context)
        rvMovieList.layoutManager = layoutManager

        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 20) {
                    removeFocus()
                }
                if(layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount-1){
                    viewLifecycleOwner.lifecycleScope.launch{
//                        viewModel.loadMoreMovies()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        observeDb()
    }

    private fun observeDb() {
        // Use an observer to monitor the db and reflect changes on adapter from the view model
        viewModel.getMovieList().observe(viewLifecycleOwner, { movieList ->
            if (movieList != null) {
                movieAdapter.setList(movieList as ArrayList<Movie>)
                context?.let {context -> movieAdapter.setContext(context) }

                rvMovieList.adapter = movieAdapter
            }
        })
    }

    private fun navigateToSingleView (movie: Movie) {
        // Navigate with safe-args
        val action = MovieListDirections.actionMovieListFragmentToMovieViewFragment(movie)

        // Perform navigation action
        findNavController().navigate(action)
    }

    // Remove the focus from search view
    private fun removeFocus() {
        hideSoftKeyboard()
        binding.mainToolbar.searchView.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        removeFocus()
    }
}