package com.example.regatas.adapters.ships

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.data.ShipData

class ShipAdapter(val shiplist: MutableList<ShipData>) : RecyclerView.Adapter<ShipViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ShipViewHolder(layoutInflater.inflate(R.layout.ship_list, parent, false))
    }

    override fun onBindViewHolder(holder: ShipViewHolder, position: Int) {
        val item = shiplist[position]
        holder.render(item)
    }

    override fun getItemCount() = shiplist.size


}