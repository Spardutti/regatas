package com.example.regatas.adapters.races

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.*
import com.example.regatas.`interface`.RaceInterface
import com.example.regatas.data.RaceData
import com.example.regatas.databinding.ListRaceBinding
import com.example.regatas.fragments.races.RaceDetailFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


class RaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ListRaceBinding.bind(view)
    private lateinit var raceInt: RaceInterface

    fun render(raceList: RaceData) {
        binding.raceName.text = raceList.name


        binding.editRaceName.setOnClickListener {
            parseRaceInfoAndNavigate(raceList, R.id.action_raceFragment_to_editRaceFragment, it)
        }



        binding.raceContainer.setOnClickListener {
            parseRaceInfoAndNavigate(raceList, R.id.action_raceFragment_to_raceDetailFragment, it)
        }

        if (raceList.isFinished) {
            binding.pillStatus.text = itemView.context.getString(R.string.race_finished)
            binding.pillStatus.backgroundTintList = itemView.context.resources.getColorStateList(R.color.finished_race)
        }

    }

    fun setOnRaceClicked(raceInterface: RaceInterface) {
        raceInt = raceInterface
    }

    fun parseRaceInfoAndNavigate(raceInfo: RaceData, toFragmentAction: Int, view: View) {
        val race = raceInt.getRaceInfo(raceInfo)
        val parsedData = Gson().toJson(race, object : TypeToken<RaceData>() {}.type)

        val bundle = Bundle()
        bundle.putString("raceInfo", parsedData)
        val fragment = RaceDetailFragment()
        fragment.arguments = bundle
        Navigation.findNavController(view)
            .navigate(toFragmentAction, bundle)
    }
}


