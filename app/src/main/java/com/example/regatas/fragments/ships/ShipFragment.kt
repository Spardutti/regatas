package com.example.regatas.fragments.ships

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
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

        shipRecyclerView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ship_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navhostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navhostFragment.navController

        return when (item.itemId) {
            R.id.delete -> {
                navController.navigate(R.id.deleteShipFragment)
                true
            }
            R.id.export -> {
//                navController.navigate(R.id.deleteShipFragment)
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