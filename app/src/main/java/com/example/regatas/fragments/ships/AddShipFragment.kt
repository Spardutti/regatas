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


class AddShipFragment : Fragment() {

    private lateinit var binding: FragmentAddShipBinding
    var shipList = mutableListOf<ShipData>()
    lateinit var serie: String
    private var imageUri: Uri? = null

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

        binding.imageAvatar.setOnClickListener { openPickerDialog() }

        binding.editPen.setOnClickListener { openPickerDialog() }

        return binding.root
    }


    /* pick image from gallery */
    private val chooseFromGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { success ->
        if (success.resultCode == RESULT_OK) {
            val selectedImg: Uri? = success?.data?.data
            binding.imageAvatar.setImageURI(selectedImg)
            imageUri = selectedImg
            requireContext().contentResolver.takePersistableUriPermission(
                selectedImg!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } else {
            Toast.makeText(requireContext(), "Por favor selecciona otra foto", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun pickImage() {
        val pickPhoto = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
        )
        pickPhoto.type = "image/*"
        chooseFromGallery.launch(pickPhoto)
    }
    /* -------- end of pick from gallery  */

    /* opens camera */
    private val openCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.imageAvatar.setImageURI(imageUri)
            } else {
                Toast.makeText(requireContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show()
            }
        }

    private fun cameraPermission() {
        val cameraPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (cameraPermission == -1) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else if (writePermission == -1) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        } else {
            takePicture.run()
        }
    }
    /* ------ end of camera */

    /* creates file/img to be stored in device */
    private val takePicture: Runnable = Runnable {
        Utils.ImageUtils.createImageFile(requireContext())?.also {
            imageUri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )
            openCamera.launch(imageUri)
        }
    }

    /* dialogs to choose between gallery and camera */
    fun openPickerDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_picker)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val cameraBtn: Button = dialog.findViewById(R.id.camera)
        val galleryBtn: Button = dialog.findViewById(R.id.gallery)

        galleryBtn.setOnClickListener {
            pickImage()
            dialog.dismiss()
        }

        cameraBtn.setOnClickListener {
            cameraPermission()
            dialog.dismiss()
        }
        dialog.show()
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
            println(ship)
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
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}