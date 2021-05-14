package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.FragmentSingleMovieViewBinding
import com.murerwa.moviesasa.models.Movie

class SingleMovieView : Fragment() {
    private var _binding: FragmentSingleMovieViewBinding? = null

    private val args: SingleMovieViewArgs by navArgs()
    private val binding get() = _binding!!

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

        val toolbar = binding.mainToolbar.root
        toolbar.title = movie.title

        val progressDrawable = CircularProgressDrawable(binding.root.context)
        progressDrawable.strokeWidth = 5f
        progressDrawable.centerRadius = 30f
        progressDrawable.start()

        Glide.with(requireContext())
            .load(movie.coverUrl)
            .placeholder(progressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_no_movie)
            .fallback(R.drawable.ic_no_movie)
            .into(binding.imvFullImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}