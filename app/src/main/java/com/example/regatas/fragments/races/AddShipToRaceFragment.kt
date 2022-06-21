package com.example.regatas.fragments.races

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.adapters.races.RaceAddShipAdapter
import com.example.regatas.adapters.raceshiplist.RaceShipListAdapter
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentAddShipToRaceBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.Delegates

class AddShipToRaceFragment : Fragment(), RaceAddShipInterface {

    lateinit var binding: FragmentAddShipToRaceBinding
    private var selectedShipList: MutableList<ShipData> = mutableListOf()
    var raceInfo: RaceData? = null
    var newRaceName: String? = null
    var newRaceDate: String? = null
    var isNewRace: Boolean = false
    var newList: MutableList<ShipData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShipToRaceBinding.inflate(inflater, container, false)

        getArgs()

        if (isNewRace) binding.btnSaveChanges.text = "Crear carrera"

        binding.btnSaveChanges.setOnClickListener {

            Utils.Races.saveRace(
                requireContext(),
                raceInfo,
                newRaceName,
                newRaceDate,
                selectedShipList,
                isNewRace
            )
            Utils.Navigation.navigateTo(
                requireActivity(),
                R.id.action_addShipToRaceFragment_to_raceFragment,
                null
            )
        }

        binding.editSearchShip.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
           filterList(p0!!)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        return binding.root
    }

    private fun filterList(letter: CharSequence) {
        var originalList = newList!!
        val newList = originalList.filter { ship -> ship.name.contains(letter, ignoreCase = true) }
        originalList = newList as MutableList<ShipData>
        val adapter = RaceAddShipAdapter(originalList)
        adapter.setOnShipSelected(this)
        binding.recyclerAddShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAddShip.adapter = adapter
    }

    fun getArgs() {
        val args = this.arguments?.getString("raceInfo")
        newRaceName = this.arguments?.getString("raceName")
        newRaceDate = this.arguments?.getString("raceDate")
        isNewRace = this.arguments?.getBoolean("NEW_RACE", false) == true
        val type = object : TypeToken<RaceData>() {}.type
        raceInfo = Gson().fromJson(args, type)

        if (raceInfo?.shipsList != null) {
            selectedShipList =
                raceInfo!!.shipsList?.map { it.copy() } as MutableList<ShipData>
        }

        addShipRecyclerList()
    }

    override fun selectShip(ship: ShipData, pos: Int) {
        if (ship.isSelected) {
            selectedShipList.remove(ship)
        } else {
            selectedShipList.add(ship)
        }
        ship.isSelected = !ship.isSelected
    }

    fun addShipRecyclerList() {
        val shipList = Prefs(requireContext()).getShipsFromStorage()
        val adapter: RaceAddShipAdapter?

         newList = selectedShipList.map { it.copy() } as MutableList<ShipData>

        newList!! += shipList?.filter { localObject ->
            selectedShipList!!.all { it.name != localObject.name }
        } as MutableList<ShipData>

        adapter = RaceAddShipAdapter(newList!!)
        adapter.setOnShipSelected(this)
        binding.recyclerAddShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAddShip.adapter = adapter
    }
}