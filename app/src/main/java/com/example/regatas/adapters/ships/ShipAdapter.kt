package com.example.regatas.adapters.ships

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.ShipInterface
import com.example.regatas.data.ShipData
import com.example.regatas.prefs.Prefs

class ShipAdapter(val shiplist: MutableList<ShipData>) : RecyclerView.Adapter<ShipViewHolder>(),
    ShipInterface {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ShipViewHolder(layoutInflater.inflate(R.layout.list_ship, parent, false))
    }

    override fun onBindViewHolder(holder: ShipViewHolder, position: Int) {
        val item = shiplist[position]
        holder.setOnShipEdit(this)
        holder.render(item)
    }

    override fun getItemCount() = shiplist.size

    override fun editShipName(context: Context, ship: ShipData, shipName: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_edit_ship)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val editText: EditText = dialog.findViewById(R.id.body)
        val yesBtn: TextView = dialog.findViewById(R.id.btnYes)
        val noBtn: TextView = dialog.findViewById(R.id.btnNo)

        editText.hint = ship.name

        noBtn.setOnClickListener { dialog.dismiss() }

        yesBtn.setOnClickListener {
            if(editText.text.isNotEmpty()) {
                ship.name = editText.text.toString()
                Prefs(it.context).updateShip(ship, shipName)
                dialog.dismiss()
                notifyDataSetChanged()
            }
        }
        dialog.show()
    }
}