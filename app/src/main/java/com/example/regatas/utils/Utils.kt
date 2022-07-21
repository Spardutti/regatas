package com.example.regatas.utils

import android.animation.AnimatorSet
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import com.example.regatas.R
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData
import com.example.regatas.prefs.Prefs
import com.example.tasker.fragments.DatePickerFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    /* creates an image file*/
    fun imagePickerConfig(fragment: Fragment): ImagePicker.Builder {
        return ImagePicker.Companion.with(fragment).crop(1f, 1f).maxResultSize(320, 320)
            .compress(1024)
            .galleryMimeTypes(  //Exclude gif images
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
    }

    /* dialogs to choose between gallery and camera */
    fun cameraGalleryDialog(
        context: Context,
        imagePicker: ImagePicker.Builder,
        startForImageResult: ActivityResultLauncher<Intent>
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_picker)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val cameraBtn: Button = dialog.findViewById(R.id.camera)
        val galleryBtn: Button = dialog.findViewById(R.id.gallery)

        galleryBtn.setOnClickListener {
            imagePicker.galleryOnly()
                .createIntent { intent -> startForImageResult.launch(intent) }
            dialog.dismiss()
        }

        cameraBtn.setOnClickListener {
            imagePicker.cameraOnly()
                .createIntent { intent -> startForImageResult.launch(intent) }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun navigateTo(activity: FragmentActivity, toFragment: Int, bundle: Bundle?) {
        val navhostFragment =
            activity.supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        val navController = navhostFragment.navController

        navController.navigate(toFragment, bundle)
    }


    fun saveRace(
        context: Context,
        raceInfo: RaceData?,
        newRaceName: String?,
        newRaceDate: String?,
        selectedShipList: MutableList<ShipData>?,
        isNewRace: Boolean
    ) {
        val currentRaces = Prefs(context).getRacesFromStorage()
        var raceList: MutableList<RaceData> = mutableListOf()
        if (currentRaces != null) raceList = currentRaces
        raceList.remove(raceInfo)
        val race = RaceData(
            newRaceName ?: raceInfo!!.name,
            newRaceDate ?: raceInfo!!.date,
            null,
            selectedShipList,
            false
        )
        raceList.add(race)
        Prefs(context).saveRace(raceList)
        if (isNewRace) Toast.makeText(context, "Carrera creada", Toast.LENGTH_SHORT).show()
        else Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
    }

}