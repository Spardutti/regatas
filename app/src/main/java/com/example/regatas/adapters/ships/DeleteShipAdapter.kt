package com.example.regatas.adapters.ships

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RemoveShipInterface
import com.example.regatas.data.ShipData
import com.example.regatas.prefs.Prefs

class DeleteShipAdapter(val shipList: MutableList<ShipData>) :
    RecyclerView.Adapter<DeleteShipViewHolder>(), RemoveShipInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DeleteShipViewHolder(layoutInflater.inflate(R.layout.deleteship_list, parent, false))
    }

    override fun onBindViewHolder(holder: DeleteShipViewHolder, position: Int) {
        val item = shipList[position]
        holder.setOnShipRemoved(this)
        holder.render(item)
    }

    override fun getItemCount() = shipList.size

    override fun removeAllShips(context: Context) {
        shipList.clear()
        Prefs(context).saveShip(shipList)
        notifyItemRangeRemoved(0, shipList.size)
    }

    override fun removeSingleShip(ship: ShipData, pos: Int, context: Context) {
        shipList.removeAt(pos)
        Prefs(context).saveShip((shipList))
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, shipList.size)
    }
}

