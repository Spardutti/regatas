package com.example.regatas.adapters.races

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.ListSimpleShipBinding

class RaceShipsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ListSimpleShipBinding.bind(view)

    fun render(ship: ShipData) {
        binding.textShipName.text = ship.name
        if (ship.avatar != "null") binding.imageShipImg.setImageURI(Uri.parse(ship.avatar))
    }

}