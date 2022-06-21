package com.example.regatas.adapters.races

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.data.ShipData

class RaceShipsAdapter(val shipList: MutableList<ShipData>) :
    RecyclerView.Adapter<RaceShipsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceShipsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RaceShipsViewHolder(layoutInflater.inflate(R.layout.list_simple_ship, parent, false))
    }

    override fun onBindViewHolder(holder: RaceShipsViewHolder, position: Int) {
        val ship = shipList[position]
        holder.render(ship)
    }

    override fun getItemCount() = shipList.size

}