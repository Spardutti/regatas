package com.example.regatas.carousel

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.example.regatas.R
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData

object CarouselHelper {

     fun winnerCarousel(shipList: MutableList<ShipData>, carousel: Carousel) {

        carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return shipList.size
            }

            override fun populate(view: View?, index: Int) {

                if (view is ViewGroup) {
                    val ship = shipList[index]
                    val container = view.children.elementAt(0) as ConstraintLayout
                    val shipAvatar = container.children.elementAt(0) as ImageView
                    val shipName = container.children.elementAt(1) as TextView

                    if (ship.avatar != "null" && ship.avatar != null) {
                        shipAvatar.setImageURI(Uri.parse(ship.avatar))
                    }
                    shipName.text = ship.name
                }
            }

            override fun onNewItem(index: Int) {
            }
        })
    }

    fun racesCarousel(racesList: MutableList<RaceData>, carousel: Carousel) {
        carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return racesList.size
            }

            override fun populate(view: View?, index: Int) {

                if (view is ViewGroup) {
                    val race = racesList[index]
                    val container = view.children.elementAt(0) as ConstraintLayout
                    val shipAvatar = container.children.elementAt(0) as ImageView
                    val shipName = container.children.elementAt(1) as TextView
                    val trophy = container.children.elementAt(2) as ImageView

                    trophy.setImageResource(R.drawable.ic_vectorship)

//                    if (race.avatar != "null" && race.avatar != null) {
//                        shipAvatar.setImageURI(Uri.parse(race.avatar))
//                    }
                    shipName.text = race.name
                }
            }

            override fun onNewItem(index: Int) {
            }
        })
    }
}