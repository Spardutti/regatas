package com.example.regatas.fragments.ships

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.adapters.ships.DeleteShipAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentDeleteShipBinding
import com.example.regatas.prefs.Prefs


class DeleteShipFragment : Fragment() {

    private lateinit var binding: FragmentDeleteShipBinding
    private var shipList = mutableListOf<ShipData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteShipBinding.inflate(inflater, container, false)
        recyclerView()

        binding.btnDeleteAll.setOnClickListener {
            DeleteShipAdapter(shipList).removeAllShips(it.context)
            binding.recyclerDeleteShip.adapter?.notifyDataSetChanged()
        }
        return binding.root
    }

    private fun recyclerView() {
        getShips()
        binding.recyclerDeleteShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDeleteShip.adapter = DeleteShipAdapter(shipList)
    }

    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        if (ships != null) shipList = ships
    }
}

