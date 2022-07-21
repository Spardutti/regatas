package com.example.regatas.fragments

import android.content.Context
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
import com.example.regatas.MainActivity
import com.example.regatas.R
import com.example.regatas.`interface`.FragmentSelectedInterface
import com.example.regatas.carousel.CarouselHelper
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentHomeBinding
import com.example.regatas.fragments.races.RaceFragment
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var selectedFragmentSelectedInterface: FragmentSelectedInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setSelectedFragmentInterface(context as FragmentSelectedInterface)

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        getRaces()

        binding.emptyHome.btnBegin.setOnClickListener {
            Utils.navigateTo(requireActivity(), R.id.addRaceFragment, null)
            selectedFragmentSelectedInterface.fragmentSelected(R.id.races)
        }

        return binding.root
    }

    private fun getShips() {
        val shipList = Prefs(requireContext()).getShipsFromStorage()
        if (shipList?.size != 0 && shipList != null) {
            shipList.sortBy { it.name }
            CarouselHelper.winnerCarousel(shipList, binding.winnerCarousel.carousel)
            CarouselHelper.recordsCarousel(shipList, binding.recordsCarousel.carousel)
        }
    }

    private fun getRaces() {
        val raceList = Prefs(requireContext()).getRacesFromStorage()
        if (raceList?.size != 0 && raceList != null) {
            CarouselHelper.racesCarousel(raceList, binding.racesCarousel.carousel)
            getShips()
        } else {
            binding.carouselContainer.visibility = View.GONE
            binding.emptyHome.root.visibility = View.VISIBLE
        }
    }

    private fun setSelectedFragmentInterface(selectedInterface: FragmentSelectedInterface){
        selectedFragmentSelectedInterface = selectedInterface
    }
}