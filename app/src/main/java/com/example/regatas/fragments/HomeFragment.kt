package com.example.regatas.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.regatas.carousel.CarouselHelper
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentHomeBinding
import com.example.regatas.prefs.Prefs


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        getShips()

        getRaces()

        return binding.root
    }

    private fun getShips() {
        val shipList = Prefs(requireContext()).getShipsFromStorage()

        if (shipList?.size != 0 && shipList != null) {
            shipList.sortBy { it.name }
            CarouselHelper.winnerCarousel(shipList, binding.winnerCarousel.carousel)
            CarouselHelper.recordsCarousel(shipList, binding.recordsCarousel.carousel)
        } else {
            binding.winnerCarousel.root.visibility = View.GONE
        }
    }

    private fun getRaces() {
        val raceList = Prefs(requireContext()).getRacesFromStorage()
        if (raceList?.size != 0) {
            CarouselHelper.racesCarousel(raceList!!, binding.racesCarousel.carousel)
        }
    }
}