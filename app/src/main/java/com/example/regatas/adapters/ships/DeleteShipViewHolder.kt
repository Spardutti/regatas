package com.example.regatas.adapters.ships

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.`interface`.RemoveShipInterface
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.DeleteshipListBinding

class DeleteShipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = DeleteshipListBinding.bind(view)
    lateinit var removeShipInt: RemoveShipInterface

    fun render(shipList: ShipData) {
        binding.textShipName.text = shipList.name
        val pos = layoutPosition

        binding.imageDeleteShip.setOnClickListener {
            removeShipInt.removeSingleShip(shipList, pos, it.context)
        }

        if (shipList.avatar != null) {
            binding.imageShipImg.setImageURI(Uri.parse(shipList.avatar))
        }
    }

    fun setOnShipRemoved(removeShipInterface: RemoveShipInterface) {
        removeShipInt = removeShipInterface
    }
}

