package com.example.regatas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.regatas.R
import com.example.regatas.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnShips.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_shipFragment)
        )

        binding.btnRaces.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_raceFragment)
        )

        return binding.root
    }
}