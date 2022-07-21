package com.example.regatas

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.regatas.`interface`.FragmentSelectedInterface
import com.example.regatas.databinding.ActivityMainBinding
import com.example.regatas.fragments.HomeFragment
import com.example.regatas.fragments.ships.ShipFragment
import com.example.regatas.utils.Utils


class MainActivity : AppCompatActivity(), FragmentSelectedInterface {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController)

        binding.menuBottom.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.home -> {
                    Utils.navigateTo(this@MainActivity, R.id.homeFragment, null)
                }
                R.id.races -> {
                    Utils.navigateTo(this@MainActivity, R.id.raceFragment, null)
                }
                R.id.boats -> {
                    Utils.navigateTo(this@MainActivity, R.id.shipFragment, null)
                }
            }
            true
        }
        binding.menuBottom.menu.findItem(R.id.home).isChecked = true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host)
        return navController.navigateUp()
    }

    /* close keyboard on screen touch */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun fragmentSelected(position: Int) {
        binding.menuBottom.menu.findItem(position).isChecked = true
    }


}