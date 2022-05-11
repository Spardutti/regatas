package com.example.regatas.data

import android.os.Parcelable
import com.example.regatas.data.ShipData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RaceData(
    var name: String,
    val date: String,
    var time: String?,
    val shipsList: @RawValue MutableList<ShipData>?,
    var isFinished: Boolean = false
): Parcelable