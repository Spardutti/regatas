package com.example.regatas.adapters.races

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.ListAddshipBinding

class RaceAddShipViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var addShipInterface: RaceAddShipInterface
    val binding = ListAddshipBinding.bind(view)


    fun render(shipList: ShipData) {
        binding.textShipName.text = shipList.name

        if (shipList.avatar != "null" && shipList.avatar != null) binding.imageShipImg.setImageURI(Uri.parse(shipList.avatar))

        binding.checkboxSelectShip.isChecked = shipList.isSelected
        binding.checkboxSelectShip.setOnClickListener {
            addShipInterface.selectShip(
                shipList,
                absoluteAdapterPosition
            )
        }
    }

    fun setOnShipSelected(shipInterface: RaceAddShipInterface) {
        addShipInterface = shipInterface
    }
}