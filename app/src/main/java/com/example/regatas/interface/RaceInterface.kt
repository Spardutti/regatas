package com.example.regatas.`interface`

import android.os.Parcelable
import com.example.regatas.adapters.races.RaceData

interface RaceInterface {
    fun getRaceInfo(raceList: RaceData): RaceData
}