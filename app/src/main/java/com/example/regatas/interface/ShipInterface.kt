package com.example.regatas.`interface`

import android.content.Context
import com.example.regatas.data.ShipData

interface ShipInterface {
    fun editShipName(context: Context, ship: ShipData, shipName: String)
}