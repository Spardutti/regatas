package com.example.regatas.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.example.regatas.R
import com.github.dhaval2404.imagepicker.ImagePicker


class Utils {
    /* creates an image file*/
    object ImageUtils {

        fun imagePickerConfig(fragment: Fragment): ImagePicker.Builder {
            return ImagePicker.Companion.with(fragment).crop(1f,1f).maxResultSize(320, 320)
                .compress(1024)
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
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