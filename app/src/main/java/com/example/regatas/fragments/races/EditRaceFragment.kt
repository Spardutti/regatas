package com.example.regatas.fragments.races

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.adapters.races.RaceAddShipAdapter
import com.example.regatas.adapters.races.RaceShipsAdapter
import com.example.regatas.adapters.ships.ShipAdapter
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentEditRaceBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.example.tasker.fragments.DatePickerFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class EditRaceFragment : Fragment() {

    lateinit var binding: FragmentEditRaceBinding
    private lateinit var raceInfo: RaceData
    private lateinit var selectedShipList: MutableList<ShipData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRaceBinding.inflate(inflater, container, false)

        /* gets race info */
        getArgs()

        binding.editName.setText(raceInfo.name)
        binding.editDate.setText(raceInfo.date)

        binding.editDate.setOnClickListener { showDatePicker() }


        binding.btnAddRace.setOnClickListener {
            Utils.saveRace(
                requireContext(),
                raceInfo,
                binding.editName.text.toString(),
                binding.editDate.text.toString(),
                selectedShipList,
                false
            )
            Utils.navigateTo(
                requireActivity(),
                R.id.action_editRaceFragment_to_raceFragment,
                null
            )
        }

        binding.btnAddShips.setOnClickListener {
            parseRaceInfoAndNavigate(
                R.id.action_editRaceFragment_to_addShipToRaceFragment,
                it
            )
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                binding.editName.text.toString()
        }

        recyclerView()

        /* Toolbar title */
        (requireActivity() as AppCompatActivity).supportActionBar?.title = raceInfo.name
        return binding.root
    }

    fun recyclerView() {
        val adapter = RaceShipsAdapter(selectedShipList)
        binding.listRaceShips.layoutManager = LinearLayoutManager(requireContext())
        binding.listRaceShips.adapter = adapter
    }

    /* Get the args from the clicked race on the previous fragment */
    fun getArgs() {
        val args = this.arguments?.getString("raceInfo")
        val type = object : TypeToken<RaceData>() {}.type
        raceInfo = Gson().fromJson(args, type)
        if (raceInfo.shipsList != null) selectedShipList =
            raceInfo.shipsList!!.map { it.copy() } as MutableList<ShipData>

    }

    private fun showDatePicker() {
        val datePicker = DatePickerFragment { day, month, year -> onSelectedDate(day, month, year) }
        datePicker.show(parentFragmentManager, "date picker")
    }

    //    updates ui with date
    private fun onSelectedDate(day: Int, month: Int, year: Int) {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        val date = Calendar.getInstance()
        date.set(Calendar.DAY_OF_MONTH, day)
        date.set(Calendar.MONTH, month)
        date.set(Calendar.YEAR, year)
        binding.editDate.setText(dateFormat.format(date.time))
    }

    fun parseRaceInfoAndNavigate(toFragmentAction: Int, view: View) {
        val newName = binding.editName.text.toString()
        val newDate = binding.editDate.text.toString()
        val parsedData = Gson().toJson(raceInfo, object : TypeToken<RaceData>() {}.type)
        val bundle = Bundle()
        bundle.putString("raceName", newName)
        bundle.putString("raceDate", newDate)
        bundle.putString("raceInfo", parsedData)
        val fragment = RaceDetailFragment()
        fragment.arguments = bundle
        Utils.navigateTo(requireActivity(), toFragmentAction, bundle)
    }

}

