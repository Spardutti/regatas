package com.example.regatas.fragments.ships

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.adapters.ships.ShipAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentShipBinding
import com.example.regatas.prefs.Prefs


class ShipFragment : Fragment() {

    private lateinit var binding: FragmentShipBinding
    private var shipList = mutableListOf<ShipData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setHasOptionsMenu(true)
        binding = FragmentShipBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.icontoolbar)
        (activity as AppCompatActivity?)!!.getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.getSupportActionBar()?.setTitle("Barcos")



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

//        binding.btnDeleteShip.setOnClickListener(
//            Navigation.createNavigateOnClickListener(R.id.action_shipFragment_to_deleteShipFragment)
//        )

        binding.delete.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_shipFragment_to_deleteShipFragment)
        )

//        binding.backArrow.setOnClickListener {
//            Navigation.createNavigateOnClickListener(R.id.action_shipFragment_to_homeFragment)
//        }


        shipRecyclerView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ship_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
            Navigation.createNavigateOnClickListener(R.id.action_shipFragment_to_deleteShipFragment)
                true
            }
            R.id.export -> {
                Toast.makeText(requireContext(), "Tapped on icon", Toast.LENGTH_SHORT).show()
//                NavigationUI.onNavDestinationSelected(item, requireView(), findNavController())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun shipRecyclerView() {
        getShips()
        binding.recyclerShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShip.adapter = ShipAdapter(shipList)
    }

    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        ships?.sortBy { ship -> ship.name }
        if (ships != null) shipList = ships
    }

    private fun filterList(letter: CharSequence) {
        var originalList = shipList
        val newList = originalList.filter { ship -> ship.name.contains(letter, ignoreCase = true) }
        originalList = newList as MutableList<ShipData>
        binding.recyclerShip.adapter = ShipAdapter(originalList)
    }
}