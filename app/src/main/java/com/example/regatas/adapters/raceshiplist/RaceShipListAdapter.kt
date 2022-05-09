package com.example.regatas.adapters.raceshiplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceShipListInterface
import com.example.regatas.data.ShipData


class RaceShipListAdapter(val shipList: MutableList<ShipData>) :
    RecyclerView.Adapter<RaceShipListViewHolder>() {
    lateinit var raceShipList: RaceShipListInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceShipListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return RaceShipListViewHolder(
            layoutInflater.inflate(
                R.layout.race_ship_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RaceShipListViewHolder, position: Int) {
        val item = shipList[position]
        holder.setOnShipTime(raceShipList)
        holder.render(item)
    }

    override fun getItemCount() = shipList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    fun setOnShipTime(raceShipListInterface: RaceShipListInterface) {
        raceShipList = raceShipListInterface
    }
}