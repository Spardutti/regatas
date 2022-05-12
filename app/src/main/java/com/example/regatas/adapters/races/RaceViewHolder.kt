package com.example.regatas.adapters.races

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.*
import com.example.regatas.`interface`.RaceInterface
import com.example.regatas.data.RaceData
import com.example.regatas.databinding.RaceListBinding
import com.example.regatas.fragments.races.RaceDetailFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


class RaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RaceListBinding.bind(view)
    private lateinit var raceInt: RaceInterface

    fun render(raceList: RaceData) {
        binding.raceName.text = raceList.name
        binding.raceDate.text = raceList.date
        if (raceList.time == null) binding.raceTime.text = "-:-"
        else {
            binding.raceTime.text = raceList.time
            binding.raceTime.typeface = Typeface.DEFAULT_BOLD
            binding.raceTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
        }

        binding.editRaceName.setOnClickListener {
            raceInt.editRace(it.context, raceList.name, raceList)
        }

        if (raceList.image != "null") {
            binding.imageCircle.setImageURI(Uri.parse(raceList.image))
        }

        binding.raceContainer.setOnClickListener {
            val race = raceInt.getRaceInfo(raceList)
            val parsedData = Gson().toJson(race, object : TypeToken<RaceData>() {}.type)

            val bundle = Bundle()
            bundle.putString("raceInfo", parsedData)
            val fragment = RaceDetailFragment()
            fragment.arguments = bundle
            Navigation.findNavController(it)
                .navigate(R.id.action_raceFragment_to_raceDetailFragment, bundle)
        }

        if (raceList.isFinished) binding.raceFlag.visibility = View.VISIBLE

    }

    fun setOnRaceClicked(raceInterface: RaceInterface) {
        raceInt = raceInterface
    }
}


