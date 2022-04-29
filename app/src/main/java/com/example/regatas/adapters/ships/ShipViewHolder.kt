package com.example.regatas.adapters.ships

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.ShipListBinding

class ShipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ShipListBinding.bind(view)

    fun render(shipList: ShipData) {
        binding.textShipName.text = shipList.name
    }
}
