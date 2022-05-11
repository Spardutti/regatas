package com.example.regatas.fragments.races

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.regatas.R
import com.example.regatas.data.RaceData
import com.example.regatas.databinding.FragmentAddRaceBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.example.tasker.fragments.DatePickerFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import java.text.SimpleDateFormat
import java.util.*

//
class AddRaceFragment : Fragment() {
    private lateinit var binding: FragmentAddRaceBinding
    private lateinit var dateTime: Date
    private var raceList = mutableListOf<RaceData>()
    private lateinit var imagePicker: ImagePicker.Builder
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRaceBinding.inflate(inflater, container, false)

        binding.editDate.setOnClickListener { showDatePicker() }

        binding.btnAddRace.setOnClickListener { createRace() }

        // imagePicker configuration
        imagePicker =
            ImagePicker.Companion.with(requireParentFragment()).compress(1024).crop()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )

        binding.imageAvatar.setOnClickListener {
            Utils.Dialog.openPickerDialog(requireContext(), imagePicker, startForImageResult)
        }

        binding.editPen.setOnClickListener {
            Utils.Dialog.openPickerDialog(requireContext(), imagePicker, startForImageResult)
        }

        return binding.root
    }

    /* start activity to choose a race photo */
    private val startForImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                imageUri = fileUri
                binding.imageAvatar.setImageURI(fileUri)
            }
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
        val name = binding.editName.text.toString()
        val date = binding.editDate.text.toString()
        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor completa los campos", Toast.LENGTH_LONG)
                .show()
        } else {
            val ships = Prefs(requireContext()).getShipsFromStorage()
            val currentRaces = Prefs(requireContext()).getRacesFromStorage()

            if (currentRaces != null) raceList = currentRaces

            if (ships != null && ships.size > 0) {
                val race = RaceData(name, date, null, ships, false, imageUri.toString())
                raceList.add(race)
                Prefs(requireContext()).saveRace(raceList)
                Toast.makeText(requireContext(), "Carrera creada", Toast.LENGTH_SHORT).show()
                resetInputs()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No hay barcos para esta carrera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun resetInputs() {
        binding.editName.setText("")
        binding.editDate.setText("")
        binding.imageAvatar.setImageURI(null)
        binding.imageAvatar.setBackgroundResource(R.drawable.upload_img)
    }
}