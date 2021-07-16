package com.murerwa.moviesasa.fragments

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
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
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.murerwa.moviesasa.adapters.MovieListAdapter
import com.murerwa.moviesasa.databinding.FragmentMovieListBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.utils.hideSoftKeyboard
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MovieList : Fragment() {
    private var _binding: FragmentMovieListBinding? = null

    private val TAG = "MOVIELIST FRAGMENT"

    private val binding get() = _binding!!

    private lateinit var rvMovieList: RecyclerView

    private lateinit var viewModel: MovieListFragmentVM

    private lateinit var searchView: TextInputEditText

    private lateinit var btnClearSearchText: ImageButton
    private lateinit var btnOpenDrawerMenu: ImageButton

    private lateinit var mMovieList: PagingData<Movie>

    var lastClickTime: Long? = null

    private val movieListAdapter = MovieListAdapter { movie: Movie -> navigateToSingleView(movie) }

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
//        fetchPosts()
        setUpSearch()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Solves bug for jumping list
        fetchPosts()
    }

    private fun setupViews() {
        viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)

        rvMovieList = binding.rvAllShows

        val layoutManager = LinearLayoutManager(context)
        rvMovieList.layoutManager = layoutManager
        rvMovieList.setHasFixedSize(true)

        rvMovieList.adapter = movieListAdapter

        searchView = binding.mainToolbar.searchView
        btnClearSearchText = binding.mainToolbar.imbClearText
        btnOpenDrawerMenu = binding.mainToolbar.imbDisplayDrawer

        binding.mainToolbar.imbClearText.setOnClickListener { searchView.setText("") }

        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 20) {
                    removeSearchViewFocus()
                }
            }
        })
    }

    private fun fetchPosts() {
        lifecycleScope.launch {
            movieListFragmentVM.fetchPosts().collect { pagingData ->
                mMovieList = pagingData
                movieListAdapter.submitData(pagingData)
            }
        }
    }

    private fun setUpSearch() {
        binding.mainToolbar.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.mainToolbar.imbClearText.visibility =
                    if (s!!.isEmpty()) View.GONE else View.VISIBLE

                // TODO:Add code to filter movies
            }
        })
    }

    private fun navigateToSingleView(movie: Movie) {
        val debounceTime = 1200L

        if (lastClickTime == null) {
            performNavigation(movie)
        } else {
            val timeNow = SystemClock.elapsedRealtime()
            val elapsedTimeSinceLastClick = timeNow - lastClickTime!!

            if (elapsedTimeSinceLastClick >= debounceTime) {
                Log.d(TAG, "Click happened")
                performNavigation(movie)
            }
        }

        lastClickTime = SystemClock.elapsedRealtime()
    }

    private fun performNavigation(movie: Movie) {
        // Navigate with safe-args
        val action = MovieListDirections.actionMovieListFragmentToMovieViewFragment(movie)

        // Perform navigation action
        findNavController().navigate(action)
    }

    private fun removeSearchViewFocus() {
        hideSoftKeyboard()

        // Remove the focus from search view
        binding.mainToolbar.searchView.clearFocus()
    }

    override fun onPause() {
        super.onPause()
        removeSearchViewFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}