package com.example.regatas.adapters.races

import android.app.Dialog
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceInterface
import com.example.regatas.prefs.Prefs

class RaceAdapter(val raceList: MutableList<RaceData>) : RecyclerView.Adapter<RaceViewHolder>(),
    RaceInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return RaceViewHolder(layoutInflater.inflate(R.layout.race_list, parent, false))
    }

    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        if (position == raceList.lastIndex) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 300
            holder.itemView.layoutParams = params
        }
        val item = raceList[position]
        holder.setOnRaceClicked(this)
        holder.render(item)
    }

    override fun getItemCount() = raceList.size

    override fun getRaceInfo(raceList: RaceData): RaceData {

        return raceList
    }

    override fun editRace(context: Context, raceName: String, raceList: RaceData) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.edit_race_dialog)
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
                raceList.name = editText.text.toString()
                Prefs(it.context).updateRace(raceList, raceName)
                dialog.dismiss()
                notifyDataSetChanged()
            }
            dialog.show()
    }
}