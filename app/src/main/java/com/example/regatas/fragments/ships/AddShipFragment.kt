package com.example.regatas.fragments.ships

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentAddShipBinding
import com.example.regatas.prefs.Prefs

class AddShipFragment : Fragment() {

    private lateinit var binding: FragmentAddShipBinding
    var shipList = mutableListOf<ShipData>()
    private var serie: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShipBinding.inflate(inflater, container, false)
        binding.btnAddShip.setOnClickListener {
            addShip()
        }
        getShips()

        binding.spinnerSerie.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) serie = p0?.selectedItem.toString()
                hideKeyboard()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                null
            }
        }
        return binding.root
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun addShip() {
        val name = binding.editName.text.toString()
        val tcf = binding.editTCF.text.toString()
        if (name.isEmpty() || tcf.isEmpty() || serie.isEmpty()) Toast.makeText(
            requireContext(),
            "Completa los campos",
            Toast.LENGTH_SHORT
        ).show()
        else {
            val ship = ShipData(tcf.toDouble(), name, serie, null)
            shipList.add(ship)
            Prefs(requireContext()).saveShip(shipList)
            Toast.makeText(requireContext(), "Barco añadido", Toast.LENGTH_SHORT).show()
            resetEdits()
        }
    }

    private fun resetEdits() {
        binding.editName.setText("")
//         binding.spinnerSerie.sele(0)
        binding.editTCF.setText("")
    }

    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        if (ships != null) shipList = ships
    }
}