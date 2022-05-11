package com.example.regatas.fragments.ships

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.regatas.BuildConfig
import com.example.regatas.R
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentAddShipBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.github.dhaval2404.imagepicker.ImagePicker


class AddShipFragment : Fragment() {

    private lateinit var binding: FragmentAddShipBinding
    var shipList = mutableListOf<ShipData>()
    lateinit var serie: String
    private var imageUri: Uri? = null

    private lateinit var imagePicker: ImagePicker.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShipBinding.inflate(inflater, container, false)

        getShips()

        binding.btnAddShip.setOnClickListener {
            addShip()
        }

        binding.spinnerSerie.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) serie = p0?.selectedItem.toString()
                hideKeyboard()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.imageAvatar.setOnClickListener { Utils.Dialog.openPickerDialog(requireContext(), imagePicker, startForProfileImageResult) }

        binding.editPen.setOnClickListener { Utils.Dialog.openPickerDialog(requireContext(), imagePicker, startForProfileImageResult) }

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

        return binding.root
    }

    /* start activity to choose a profile photo */
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                val fileUri = data?.data!!
                imageUri = fileUri
                binding.imageAvatar.setImageURI(fileUri)
            }
        }

    /* creates a new ship entry */
    private fun addShip() {
        val name = binding.editName.text.toString()
        val tcf = binding.editTCF.text.toString()
        if (name.isEmpty() || tcf.isEmpty() || serie.isEmpty()) Toast.makeText(
            requireContext(),
            "Completa los campos",
            Toast.LENGTH_SHORT
        ).show()
        else {
            val ship = ShipData(tcf.toDouble(), name, serie, null, false, imageUri.toString())
            shipList.add(ship)
            Prefs(requireContext()).saveShip(shipList)
            Toast.makeText(requireContext(), "Barco añadido", Toast.LENGTH_SHORT).show()
            resetEdits()
        }
    }

    /* get ships from local storage */
    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        if (ships != null) shipList = ships
    }

    private fun resetEdits() {
        binding.editName.setText("")
        binding.editTCF.setText("")
        binding.imageAvatar.setImageURI(null)
        binding.imageAvatar.setBackgroundResource(R.drawable.upload_img)
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}