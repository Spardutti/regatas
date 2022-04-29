package com.example.regatas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.regatas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = this.findNavController(R.id.nav_host)



        NavigationUI.setupActionBarWithNavController(this, navController)

//        val homeFragment = HomeFragment()
////        val raceFragment = RaceFragment()
//        val shipFragment = ShipFragment()
////        makeCurrentFragment(homeFragment)


//        binding.nav.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.home -> makeCurrentFragment(homeFragment)
////                R.id.ic_history -> makeCurrentFragment(raceFragment)
//                R.id.ships -> makeCurrentFragment(shipFragment)
//            }
//            true
//        }
//
//
//        binding.nav.menu.findItem(R.id.home).isChecked = true

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host)
        return navController.navigateUp()
    }

//    private fun makeCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.main_layout, fragment)
//            addToBackStack(null)
//            commit()q
//        }

}