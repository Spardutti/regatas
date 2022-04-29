package com.example.regatas.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.regatas.constants.Constants
import com.example.regatas.data.ShipData
import com.example.regatas.adapters.races.RaceData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Prefs(val context: Context) {

    private val storage: SharedPreferences =
        context.getSharedPreferences(Constants.REGATAS_PREFS, 0)

    fun saveShip(shipList: MutableList<ShipData>) {
        val gson = Gson()
        val json = gson.toJson(shipList)//converting list to Json

        storage.edit().putString(Constants.SHIP_LIST, json).apply()
    }

    fun getShipsFromStorage(): MutableList<ShipData>? {
        val gson = Gson()
        val json = storage.getString(Constants.SHIP_LIST, null)
        val type = object : TypeToken<MutableList<ShipData>>() {}.type
        if (json != null) {
            return gson.fromJson(json, type)
        }
        return null
    }

    fun saveRace(raceList: MutableList<RaceData>) {
        val gson = Gson()
        val json = gson.toJson(raceList)
        storage.edit().putString(Constants.RACE_LIST, json).apply()
    }

    fun getRacesFromStorage(): MutableList<RaceData>? {
        val gson = Gson()
        val json = storage.getString(Constants.RACE_LIST, null)
        val type = object : TypeToken<MutableList<RaceData>>() {}.type
        if (json != null) return gson.fromJson(json, type)
        return null
    }

    fun getSingleRace(race: RaceData, raceName: String) {
        val gson = Gson()
        val json = storage.getString(Constants.RACE_LIST, null)
        val type = object : TypeToken<MutableList<RaceData>>() {}.type
        val info: MutableList<RaceData> = gson.fromJson(json, type)
        val iterator = info.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if(item.name == raceName) {
                iterator.remove()
            }
        }
        info.add(race)
        saveRace(info)
    }
}