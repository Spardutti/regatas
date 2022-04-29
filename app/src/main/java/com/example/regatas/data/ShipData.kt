package com.example.regatas.data

data class ShipData(
    val tcf: Double,
    val name: String,
    val serie: String,
    var time: String?,
    var isFinished: Boolean = false
)