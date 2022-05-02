package com.example.regatas.adapters.ships

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.data.ShipData

class ShipAdapter(val shiplist: MutableList<ShipData>) : RecyclerView.Adapter<ShipViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ShipViewHolder(layoutInflater.inflate(R.layout.ship_list, parent, false))
    }

    override fun onBindViewHolder(holder: ShipViewHolder, position: Int) {
        if (position == shiplist.lastIndex){
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 300
            holder.itemView.layoutParams = params
        }
        val item = shiplist[position]
        holder.render(item)
    }

    override fun getItemCount() = shiplist.size


}