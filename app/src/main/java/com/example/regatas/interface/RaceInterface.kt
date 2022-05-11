package com.example.regatas.`interface`

import android.content.Context
import com.example.regatas.data.RaceData

interface RaceInterface {
    fun getRaceInfo(raceList: RaceData): RaceData

    fun editRace(context: Context, raceName: String, raceList: RaceData)
}