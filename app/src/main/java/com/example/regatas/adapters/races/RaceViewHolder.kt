package com.example.regatas.adapters.races

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.*
import com.example.regatas.`interface`.RaceInterface
import com.example.regatas.databinding.RaceListBinding
import com.example.regatas.fragments.races.RaceDetailFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RaceListBinding.bind(view)
    lateinit var raceInt: RaceInterface

    fun render(raceList: RaceData) {
        binding.raceName.text = raceList.name
        binding.raceDate.text = raceList.date
        binding.raceTime.text = raceList.time
        val pos = layoutPosition

        binding.editRaceName.setOnClickListener {
            raceInt.editRace(it.context, raceList.name, raceList)
        }

        binding.raceItem.setOnClickListener {
            val race = raceInt.getRaceInfo(raceList)
            val parsedData = Gson().toJson(race, object : TypeToken<RaceData>() {}.type)

            val bundle = Bundle()
            bundle.putString("raceInfo", parsedData)
            val fragment = RaceDetailFragment()
            fragment.arguments = bundle
            Navigation.findNavController(it)
                .navigate(R.id.action_raceFragment_to_raceDetailFragment, bundle)
        }
    }

    fun setOnRaceClicked(raceInterface: RaceInterface) {
        raceInt = raceInterface
    }
}


