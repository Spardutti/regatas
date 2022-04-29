package com.example.tasker.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), /* this -> */DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dates: Calendar = Calendar.getInstance()
        val day = dates.get(Calendar.DAY_OF_MONTH)
        val month = dates.get(Calendar.MONTH)
        val year = dates.get(Calendar.YEAR)

        val dialog = DatePickerDialog(activity as Context, this, year, month, day)
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        return dialog
    }
}