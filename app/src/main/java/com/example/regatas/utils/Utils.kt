package com.example.regatas.utils

import android.content.Context
import android.os.Environment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.regatas.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    /* creates an image file*/
    object ImageUtils {
        lateinit var currentPhotoPath: String

        fun createImageFile(context: Context): File? {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }
    }


}