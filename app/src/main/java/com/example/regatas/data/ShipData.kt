package com.example.regatas.data

data class ShipData(
    val tcf: Double,
    var name: String,
    val serie: String,
    var time: String?,
    var isFinished: Boolean = false,
    var avatar: String?
)