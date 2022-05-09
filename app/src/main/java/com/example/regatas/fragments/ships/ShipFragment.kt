package com.example.regatas.fragments.ships

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.adapters.ships.ShipAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentShipBinding
import com.example.regatas.prefs.Prefs
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class ShipFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            importShips()
        } else {
            // Permission request was denied.
            requestPermissions()
        }
    }

    private lateinit var binding: FragmentShipBinding
    private var shipList = mutableListOf<ShipData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentShipBinding.inflate(inflater, container, false)

        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0!!)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.btnAddShip.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_shipFragment_to_addShipFragment)
        )

        getShips()

        return binding.root
    }

    /* request persmision to read files */
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            importShips()

        } else requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ship_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val delete = menu.findItem(R.id.delete)
        if(shipList.size == 0) delete.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navhostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navhostFragment.navController

        return when (item.itemId) {
            R.id.delete -> {
                navController.navigate(R.id.action_shipFragment_to_deleteShipFragment)
                true
            }
            R.id.importShips -> {
                requestPermissions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shipRecyclerView(shipList: MutableList<ShipData>) {
        binding.recyclerShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShip.adapter = ShipAdapter(shipList)
    }

    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        ships?.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
        if (ships != null) shipList = ships
        shipRecyclerView(shipList)
    }

    private fun filterList(letter: CharSequence) {
        var originalList = shipList
        val newList = originalList.filter { ship -> ship.name.contains(letter, ignoreCase = true) }
        originalList = newList as MutableList<ShipData>
        binding.recyclerShip.adapter = ShipAdapter(originalList)
    }

    fun importShips() {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) { // Step 1: When a result has been received, check if it is the result for READ_IN_FILE
            if (resultCode == RESULT_OK) { // Step 2: Check if the operation to retrieve the activity's result is successful
                // Attempt to retrieve the file
                try {
                    data?.data?.let {
                        context?.contentResolver?.openInputStream(it)

                    }?.let {
                        val r = BufferedReader(InputStreamReader(it))
                        while (true) {

                            val line: String? = r.readLine() ?: break
                            val split = line?.split(",")
                            val name = split?.get(0)
                            val serie = split?.get(1)

                            if (split?.get(2)?.toDoubleOrNull() != null) {
                                val tcf = split?.get(2)?.toDoubleOrNull()
                                val ship = ShipData(tcf!!, name!!, serie!!, null)
                                shipList.add(ship)
                            }
                        }
                        Prefs(requireContext()).saveShip(shipList)
                        shipRecyclerView(shipList)
                        activity?.invalidateOptionsMenu()
                        Toast.makeText(requireContext(), "Barcos importados con exito", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { // If the app failed to attempt to retrieve the error file, throw an error alert
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error abriendo el archivo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}