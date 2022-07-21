package com.example.regatas.fragments.races

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.adapters.races.RaceAdapter
import com.example.regatas.data.RaceData
import com.example.regatas.databinding.FragmentRaceBinding
import com.example.regatas.prefs.Prefs

class RaceFragment : Fragment() {

    lateinit var binding: FragmentRaceBinding
    private var raceList = mutableListOf<RaceData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRaceBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        binding.btnAddRace.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_raceFragment_to_addRaceFragment)
        )

        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0!!)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        getRaces()

        raceRecyclerView()

        return binding.root
    }


    private fun raceRecyclerView() {
        binding.recyclerRaces.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRaces.adapter = RaceAdapter(raceList)
    }

    private fun getRaces() {
        val races = Prefs(requireContext()).getRacesFromStorage()
        if (races != null) {
            races.sortWith(compareBy<RaceData, String>(String.CASE_INSENSITIVE_ORDER) { it.isFinished.toString() }.thenBy { it.date }
                .thenBy { it.name })
            raceList = races
        }
    }

    private fun filterList(letter: CharSequence) {
        var originalList = raceList
        val newList = originalList.filter { ship -> ship.name.contains(letter, ignoreCase = true) }
        originalList = newList as MutableList<RaceData>
        binding.recyclerRaces.adapter = RaceAdapter(originalList)
    }

}