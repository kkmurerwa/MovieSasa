package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.murerwa.moviesasa.adapters.MovieListAdapter
import com.murerwa.moviesasa.databinding.FragmentMovieListBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.utils.hideSoftKeyboard
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieList : Fragment() {
    private var _binding: FragmentMovieListBinding? = null

    private val binding get() = _binding!!

    private lateinit var rvMovieList : RecyclerView

    private lateinit var viewModel: MovieListFragmentVM

    private lateinit var searchView: TextInputEditText

    private lateinit var btnClearSearchText: ImageButton
    private lateinit var btnOpenDrawerMenu: ImageButton

    private lateinit var mMovieList: PagingData<Movie>

    private lateinit var movieListAdapter: MovieListAdapter

    private val movieListFragmentVM: MovieListFragmentVM by lazy {
        ViewModelProvider(this).get(MovieListFragmentVM::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        fetchPosts()

//        observeDb()

        setUpSearch()
    }

    private fun setupViews() {
        viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)

        movieListAdapter = MovieListAdapter { movie: Movie -> navigateToSingleView(movie)}

        rvMovieList = binding.rvAllShows

        rvMovieList.adapter = movieListAdapter

        searchView = binding.mainToolbar.searchView
        btnClearSearchText = binding.mainToolbar.imbClearText
        btnOpenDrawerMenu = binding.mainToolbar.imbDisplayDrawer

        val layoutManager = LinearLayoutManager(context)
        rvMovieList.layoutManager = layoutManager

        binding.mainToolbar.imbClearText.setOnClickListener { searchView.setText("") }

        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 20) {
                    removeFocus()
                }
            }
        })
    }

    private fun fetchPosts() {
        lifecycleScope.launch {
            movieListFragmentVM.fetchPosts().collectLatest { pagingData ->
                mMovieList = pagingData

                movieListAdapter.submitData(mMovieList)
            }
        }
    }

    private fun setUpSearch() {
        binding.mainToolbar.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.mainToolbar.imbClearText.visibility = if (s!!.isEmpty()) View.GONE else View.VISIBLE

                if (s.isNotEmpty()) {
                    val searchString = s.toString().toLowerCase()

                    val tempMutableList = mMovieList.filter {
                        (it.title.toLowerCase().contains(searchString))
                    }

                    lifecycleScope.launch {
                        movieListAdapter.submitData(tempMutableList)
                    }
                } else {
                    fetchPosts()
                }
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