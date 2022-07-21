package com.example.regatas.fragments.races

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.adapters.races.RaceAddShipAdapter
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentAddRaceBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.example.tasker.fragments.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

//
class AddRaceFragment : Fragment() {
    private lateinit var binding: FragmentAddRaceBinding
    private var raceList = mutableListOf<RaceData>()
    private var selectedShipList: MutableList<ShipData>? = mutableListOf()
    var raceInfo: RaceData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRaceBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        binding.editDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnAddShips.setOnClickListener {
            if (binding.editName.text.isNotEmpty() && binding.editDate.text.isNotEmpty()) {
                parseRaceInfoAndNavigate(
                    R.id.action_addRaceFragment_to_addShipToRaceFragment,
                    it
                )
//                (requireActivity() as AppCompatActivity).supportActionBar?.title =
//                    binding.editName.text.toString()
            }
            else {
                Snackbar.make(requireView(), "Please enter a name and date", Snackbar.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun parseRaceInfoAndNavigate(toFragmentAction: Int, view: View) {
        val newName = binding.editName.text.toString()
        val newDate = binding.editDate.text.toString()
        raceInfo = RaceData(newName, newDate, null, null)
        val parsedData = Gson().toJson(raceInfo, object : TypeToken<RaceData>() {}.type)
        val bundle = Bundle()
        bundle.putString("raceName", newName)
        bundle.putString("raceDate", newDate)
        bundle.putString("raceInfo", parsedData)
        bundle.putBoolean("NEW_RACE", true)
        val fragment = RaceDetailFragment()
        fragment.arguments = bundle
        Utils.navigateTo(requireActivity(), toFragmentAction, bundle)
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


    fun resetInputs() {
        binding.editName.setText("")
        binding.editDate.setText("")
    }
}