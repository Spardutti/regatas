package com.example.regatas.adapters.races

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceInterface

class RaceAdapter(val raceList: MutableList<RaceData>) : RecyclerView.Adapter<RaceViewHolder>(),
    RaceInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return RaceViewHolder(layoutInflater.inflate(R.layout.race_list, parent, false))
    }

    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        val item = raceList[position]
        holder.setOnRaceClicked(this)
        holder.render(item)
    }

    override fun getItemCount() = raceList.size

    override fun getRaceInfo(raceList: RaceData): RaceData {

        return raceList
    }
}