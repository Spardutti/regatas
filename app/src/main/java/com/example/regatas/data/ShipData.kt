package com.example.regatas.data

import android.net.Uri

data class ShipData(
    val tcf: Double,
    var name: String,
    val serie: String?,
    var time: String?,
    var isFinished: Boolean = false,
    var avatar: String?,
    var isSelected: Boolean = false,
)