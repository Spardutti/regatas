package com.example.regatas.`interface`

import android.content.Context
import com.example.regatas.data.ShipData

interface RemoveShipInterface {
    fun removeAllShips(context: Context)

    fun removeSingleShip(ship: ShipData, pos: Int, context: Context)
}