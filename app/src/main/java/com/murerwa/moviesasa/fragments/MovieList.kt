package com.murerwa.moviesasa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.FragmentFirstBinding

class MovieList : Fragment() {
    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val toolbar = binding.mainToolbar.root
        toolbar.title = "Home"
//        toolbar.setNavigationOnClickListener {
//            Navigation.findNavController(view).popBackStack()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}