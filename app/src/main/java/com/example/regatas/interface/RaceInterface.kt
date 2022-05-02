package com.example.regatas.`interface`

import android.content.Context
import android.os.Parcelable
import com.example.regatas.adapters.races.RaceData

interface RaceInterface {
    fun getRaceInfo(raceList: RaceData): RaceData

    fun editRace(context: Context, raceName: String, raceList: RaceData)
}