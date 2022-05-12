package com.example.regatas.adapters.ships

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.`interface`.ShipInterface
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.ShipListBinding

class ShipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ShipListBinding.bind(view)
    private lateinit var editShipInterface: ShipInterface

    fun render(shipList: ShipData) {
        binding.textShipName.text = shipList.name
        if(shipList.avatar != "null") {
            binding.imageShipImg.setImageURI(Uri.parse(shipList.avatar))
        }

        binding.imageEditShip.setOnClickListener {
            editShipInterface.editShipName(it.context, shipList, shipList.name)
        }
    }

    fun setOnShipEdit(shipInterface: ShipInterface) {
        editShipInterface = shipInterface
    }
}
