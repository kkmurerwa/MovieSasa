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

    private var mMovieList: MutableList<Movie> = ArrayList()

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

        viewModel = ViewModelProvider(requireActivity()).get(MovieListFragmentVM::class.java)

        movieListAdapter = MovieListAdapter { movie: Movie -> navigateToSingleView(movie)}

        searchView = binding.mainToolbar.searchView
        btnClearSearchText = binding.mainToolbar.imbClearText
        btnOpenDrawerMenu = binding.mainToolbar.imbDisplayDrawer

        rvMovieList = binding.rvAllShows

        val layoutManager = LinearLayoutManager(context)
        rvMovieList.layoutManager = layoutManager

        binding.mainToolbar.imbClearText.setOnClickListener {
            searchView.setText("")
        }

        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 20) {
                    removeFocus()
                }
            }
        })

        setupViews()

        fetchPosts()

//        observeDb()

//        setUpSearch()
    }

    private fun setUpSearch() {
        binding.mainToolbar.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    binding.mainToolbar.imbClearText.visibility = View.VISIBLE

                    val searchString = binding.mainToolbar.searchView.text.toString()

                    val tempMutableList: MutableList<Movie> = ArrayList()

                    mMovieList.forEach {
                        if (it.title.toLowerCase().contains(searchString.toLowerCase())){
                            tempMutableList.add(it)
                        }
                    }

//                    movieAdapter.setList(tempMutableList as ArrayList<Movie>)
//
//                    movieAdapter.notifyDataSetChanged()
                } else {
                    binding.mainToolbar.imbClearText.visibility = View.GONE

//                    movieAdapter.setList(mMovieList as ArrayList<Movie>)
//
//                    movieAdapter.notifyDataSetChanged()
                }
            }
        })


//        binding.mainToolbar.searchView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
//
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                removeFocus()
//                val searchString = binding.mainToolbar.searchView.text.toString()
//
//                var tempMutableList: MutableList<Movie> = ArrayList()
//
//                mMovieList.forEach {
//                    if (it.title.contains(searchString)){
//                       tempMutableList.add(it)
//                    }
//                }
//
//                movieAdapter.setList(tempMutableList as ArrayList<Movie>)
//
//                movieAdapter.notifyDataSetChanged()
//
//                return@OnEditorActionListener true
//            }
//            false
//        })
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

    private fun setupViews() {
        rvMovieList.adapter = movieListAdapter
    }

    private fun fetchPosts() {
        lifecycleScope.launch {
            movieListFragmentVM.fetchPosts().collectLatest { pagingData ->

                Log.d("PAGING", pagingData.toString())
                movieListAdapter.submitData(pagingData)
            }
        }
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