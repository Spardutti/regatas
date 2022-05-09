package com.example.regatas.fragments.races

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.regatas.databinding.FragmentAddRaceBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.adapters.races.RaceData
import com.example.tasker.fragments.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

//
class AddRaceFragment : Fragment() {
    private lateinit var binding: FragmentAddRaceBinding
    lateinit var dateTime: Date
    private var raceList = mutableListOf<RaceData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRaceBinding.inflate(inflater, container, false)

        binding.editDate.setOnClickListener { showDatePicker() }

        binding.btnAddRace.setOnClickListener { createRace() }


        return binding.root
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
        dateTime = date.time
        binding.editDate.setText(dateFormat.format(date.time))
    }

    private fun createRace() {
        var name = binding.editName.text.toString()
        var date = binding.editDate.text.toString()
        if (name.isEmpty() || date.isEmpty()) {

            Toast.makeText(requireContext(), "Por favor completa los campos", Toast.LENGTH_LONG)
                .show()
        } else {
            val ships = Prefs(requireContext()).getShipsFromStorage()
            val currentRaces = Prefs(requireContext()).getRacesFromStorage()

            if (currentRaces != null) raceList = currentRaces

            if (ships != null && ships.size > 0) {
                val race = RaceData(name, date, null, ships)
                raceList.add(race)
                Prefs(requireContext()).saveRace(raceList)
                Toast.makeText(requireContext(), "Carrera creada", Toast.LENGTH_SHORT).show()
                binding.editName.setText("")
                binding.editDate.setText("")
            } else {
                Toast.makeText(
                    requireContext(),
                    "No hay barcos para esta carrera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}