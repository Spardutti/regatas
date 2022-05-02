package com.example.regatas.fragments.races

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.`interface`.RaceShipListInterface
import com.example.regatas.adapters.races.RaceData
import com.example.regatas.adapters.raceshiplist.RaceShipListAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentRaceDetailBinding
import com.example.regatas.prefs.Prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RaceDetailFragment : Fragment(), RaceShipListInterface {

    lateinit var binding: FragmentRaceDetailBinding
    private lateinit var raceInfo: RaceData
    private var isActive = false
    lateinit var timer: Chronometer
    lateinit var startStop: ImageView
    var timeWhenStopped: Long = 0
    var shipList = mutableListOf<ShipData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        binding = FragmentRaceDetailBinding.inflate(inflater, container, false)
        val startStopBtn = binding.imageStartStop
        timer = binding.chronoRaceTime
        startStop = binding.imageStartStop

        startStopBtn.setOnClickListener { chronoController() }

        getArgs()

        initRecyclerView()
        val title = raceInfo.name
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title


        return binding.root
    }

    fun getArgs() {
        val args = this.arguments?.getString("raceInfo")
        val type = object : TypeToken<RaceData>() {}.type
        raceInfo = Gson().fromJson(args, type)

        binding.title.text = raceInfo.name
        if (raceInfo.time != null) {
            binding.chronoRaceTime.text = raceInfo.time
            binding.imageStartStop.visibility = View.GONE
        }

        val ships = raceInfo.shipsList
        shipList = ships!!
    }

    fun chronoController() {
        if (!isActive) startTimer()
        else showDialog(timer.text.toString())
    }

    fun startTimer() {
        startStop.setImageResource(R.drawable.ic_stop)
        timer.base = SystemClock.elapsedRealtime() + timeWhenStopped
        timer.start()
        isActive = true
    }

    fun initRecyclerView() {
        if (raceInfo.isFinished) {
            shipList.sortBy { ship -> ship.time }
        } else {
            shipList.sortWith(compareBy({ it.name }, { !it.isFinished }))
        }
        val adapter = RaceShipListAdapter(shipList)
        adapter.setOnShipTime(this)
        binding.recyclerRaceShipList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRaceShipList.adapter = adapter
    }

    override fun onShipStopped(pos: Int) {
        shipList[pos].time = timer.text.toString()
    }

    fun completeRaceAndUpdateInfo(time: String) {
        startStop.setImageResource(R.drawable.ic_timer_start)
        isActive = false
        timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
        raceInfo.time = time
        raceInfo.isFinished = true
        binding.imageStartStop.visibility = View.GONE
        Prefs(requireContext()).getSingleRace(raceInfo, raceInfo.name)
    }

    private fun showDialog(title: String) {
        timer.stop()
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.finish_race_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val body = dialog.findViewById(R.id.body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.btnYes) as Button
        val noBtn = dialog.findViewById(R.id.btnNo) as TextView
        yesBtn.setOnClickListener {
            completeRaceAndUpdateInfo(title)
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            timer.start()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.race_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navhostFragment =
//            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host) as NavHostFragment
//        val navController = navhostFragment.navController
//
//        return when (item.itemId) {
//            R.id.delete -> {
//                navController.navigate(R.id.deleteShipFragment)
//                true
//            }
//            R.id.export -> {
////                navController.navigate(R.id.deleteShipFragment)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}