package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.FragmentSingleMovieViewBinding
import com.murerwa.moviesasa.models.Movie
import com.murerwa.moviesasa.viewmodels.SingleMovieFragmentVM


class SingleMovieView : Fragment() {
    private var _binding: FragmentSingleMovieViewBinding? = null

    private val args: SingleMovieViewArgs by navArgs()
    private val binding get() = _binding!!

    private lateinit var viewModel: SingleMovieFragmentVM

    private lateinit var movie: Movie

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleMovieViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie = args.movie!!

        viewModel = ViewModelProvider(requireActivity()).get(SingleMovieFragmentVM::class.java)

        val toolbar = binding.mainToolbar.root
        toolbar.title = movie.title
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Progress loader for glide
        val progressDrawable = CircularProgressDrawable(binding.root.context)
        progressDrawable.strokeWidth = 5f
        progressDrawable.centerRadius = 30f
        progressDrawable.start()

        // Load image with glide
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
            .placeholder(progressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.ic_no_movie)
            .fallback(R.drawable.ic_no_movie)
            .into(binding.imvFullImage)

        // Populate text views
        binding.tvFullMovieDesc.text = movie.overview

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        fetchAllGenres()
    }

    private fun fetchAllGenres() {
        viewModel.getGenreList().observe(viewLifecycleOwner, { genreList ->
            binding.cgFilmGenres.removeAllViews()
            genreList?.forEach {
                if (movie.genre_ids.contains(it.id)) {
                    val chip = Chip(requireContext())
                    chip.text = it.name
                    chip.id = it.id

                    binding.cgFilmGenres.addView(chip)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}