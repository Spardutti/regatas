package com.example.regatas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.regatas.R
import com.example.regatas.databinding.FragmentHomeBinding
import com.example.regatas.utils.Utils

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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