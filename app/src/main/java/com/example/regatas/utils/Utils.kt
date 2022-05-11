package com.example.regatas.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.regatas.R
import com.github.dhaval2404.imagepicker.ImagePicker
import org.jetbrains.annotations.Contract
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

    /* dialogs to choose between gallery and camera */
    object Dialog {

        fun openPickerDialog(
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
    }

}