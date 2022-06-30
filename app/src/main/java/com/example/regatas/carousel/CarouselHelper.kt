package com.example.regatas.carousel

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
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

    fun racesCarousel(
        racesList: MutableList<RaceData>,
        carousel: Carousel,
    ) {
        carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return racesList.size
            }

            override fun populate(view: View?, index: Int) {

                if (view is ViewGroup) {
                    val race = racesList[index]
                    val container = view.children.elementAt(0) as ConstraintLayout
                    val raceAvatar = container.children.elementAt(0) as ImageView
                    val raceName = container.children.elementAt(1) as TextView
                    val raceDate = container.children.elementAt(2) as TextView
                    val shipCount = container.children.elementAt(4) as TextView

                    raceName.text = race.name
                    raceDate.text = race.date
                    shipCount.text = race.shipsList?.size.toString()

//                    if (race.avatar != "null" && race.avatar != null) {
//                        shipAvatar.setImageURI(Uri.parse(race.avatar))
//                    }
                }
            }

            override fun onNewItem(index: Int) {
            }
        })
    }

    fun recordsCarousel(shipList: MutableList<ShipData>, carousel: Carousel) {
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
                    val shipRecord = container.children.elementAt(2) as TextView
                    val icon = container.children.elementAt(3) as ImageView
                    val shipCount = container.children.elementAt(4) as TextView

                    shipName.text = ship.name
                    icon.visibility = View.GONE
                    shipCount.visibility = View.GONE

                    if (ship.avatar != "null" && ship.avatar != null) {
                        shipAvatar.setImageURI(Uri.parse(ship.avatar))
                    }
                }
            }

            override fun onNewItem(index: Int) {
            }
        })
    }
}