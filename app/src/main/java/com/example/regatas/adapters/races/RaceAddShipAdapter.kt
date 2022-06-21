package com.example.regatas.adapters.races

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.data.ShipData

class RaceAddShipAdapter(val shipList: MutableList<ShipData>) :
    RecyclerView.Adapter<RaceAddShipViewHolder>() {
    lateinit var addShipInterface: RaceAddShipInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceAddShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RaceAddShipViewHolder(layoutInflater.inflate(R.layout.list_addship, parent, false))
    }

    override fun onBindViewHolder(holder: RaceAddShipViewHolder, position: Int) {
        val ship = shipList[position]
        holder.setOnShipSelected(addShipInterface)
        holder.render(ship)
    }

    override fun getItemCount() = shipList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnShipSelected(raceAddShipInterface: RaceAddShipInterface) {
        addShipInterface = raceAddShipInterface
    }
}