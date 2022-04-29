package com.example.regatas.adapters.raceshiplist

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


        val pos = layoutPosition

        if (shipList.isFinished) {
            binding.imageFinish.setImageResource(R.drawable.ic_edit)
        } else {
            binding.imageFinish.setOnClickListener {
                raceShipList.onShipStopped(pos)
                binding.textTime.text = "Tiempo: ${shipList.time}"
                shipList.isFinished = true
                binding.imageFinish.setImageResource(R.drawable.ic_edit)
            }
        }
    }

    fun setOnShipTime(raceShipListInterface: RaceShipListInterface) {
        raceShipList = raceShipListInterface
    }
}