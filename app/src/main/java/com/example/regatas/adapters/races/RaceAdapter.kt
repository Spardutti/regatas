package com.example.regatas.adapters.races

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceInterface
import com.example.regatas.data.RaceData
import com.example.regatas.prefs.Prefs

class RaceAdapter(val raceList: MutableList<RaceData>) : RecyclerView.Adapter<RaceViewHolder>(),
    RaceInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RaceViewHolder(layoutInflater.inflate(R.layout.list_race, parent, false))
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

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun editRace(context: Context, raceName: String, raceList: RaceData) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_edit_race)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val editText = dialog.findViewById(R.id.body) as EditText
        val yesBtn: TextView = dialog.findViewById(R.id.btnYes)
        val noBtn: TextView = dialog.findViewById(R.id.btnNo)

        editText.hint = raceName

        noBtn.setOnClickListener { dialog.dismiss() }

        yesBtn.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                raceList.name = editText.text.toString()
                Prefs(it.context).updateRace(raceList, raceName)
                notifyDataSetChanged()
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}