package com.example.regatas.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.regatas.R

class Utils {


    fun goToFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            replace(R.id.main_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }
}