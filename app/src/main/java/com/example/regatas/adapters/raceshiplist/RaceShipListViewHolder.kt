package com.example.regatas.adapters.raceshiplist

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceShipListInterface
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.RaceShipListBinding

class RaceShipListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RaceShipListBinding.bind(view)

    lateinit var raceShipList: RaceShipListInterface

    fun render(shipList: ShipData) {
        binding.textShipName.text = shipList.name
        if (shipList.time != null) binding.textTime.text = "Tiempo: ${shipList.time}"

        if (shipList.isFinished) {
            binding.imageFinish.setImageResource(R.drawable.ic_finish_race)
        } else {
            binding.imageFinish.setOnClickListener {
                raceShipList.onShipStopped(absoluteAdapterPosition)
            }
        }

        if(shipList.avatar != null) {
            binding.imageShipImg.setImageURI(Uri.parse(shipList.avatar))
        }
    }

    fun setOnShipTime(raceShipListInterface: RaceShipListInterface) {
        raceShipList = raceShipListInterface
    }
}