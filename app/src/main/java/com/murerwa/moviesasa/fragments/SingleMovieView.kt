package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.FragmentSecondBinding
import com.murerwa.moviesasa.models.Movie
import androidx.navigation.fragment.navArgs
import com.murerwa.moviesasa.viewmodels.MovieListFragmentVM

class SingleMovieView : Fragment() {
    private var _binding: FragmentSecondBinding? = null

    val args: SingleMovieViewArgs by navArgs()
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
//            findNavController().navigateUp()
            args.movie?.let { it1 -> Log.d("Title", it1.title) }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}