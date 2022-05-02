package com.example.regatas.fragments.races

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    private var shipsFinishedCount = 0

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

        /* ONChange listener for editText */
        binding.editSearchShip.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0!!)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /* Toolbar title */
        (requireActivity() as AppCompatActivity).supportActionBar?.title = raceInfo.name

        return binding.root
    }

    /* Get the args from the click race on the previous fragment */
    fun getArgs() {
        val args = this.arguments?.getString("raceInfo")
        val type = object : TypeToken<RaceData>() {}.type
        raceInfo = Gson().fromJson(args, type)

        if (raceInfo.time != null) {
            binding.chronoRaceTime.text = raceInfo.time
            binding.imageStartStop.visibility = View.GONE
        }

        val ships = raceInfo.shipsList
        shipList = ships!!
    }

    /* start stop controller */
    fun chronoController() {
        if (!isActive) startTimer()
        else finishRaceDialog(timer.text.toString())
    }

    fun startTimer() {
        startStop.setImageResource(R.drawable.ic_stopstop)
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

    /* Assigns the current time to the clicked ships, and set isFinished to true.
    * if all ships are isFinished, ends the race */
    override fun onShipStopped(pos: Int) {
        shipList[pos].time = timer.text.toString()
        shipList[pos].isFinished = true
        shipsFinishedCount++
        if (shipsFinishedCount == shipList.size) {
            completeRaceAndUpdateInfo(timer.text.toString())
            timer.stop()
            Toast.makeText(requireContext(), "Carrera terminada", Toast.LENGTH_SHORT).show()
        }
    }

    /* Opens a confirmation dialog to finish the race */
    private fun finishRaceDialog(title: String) {
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
        val yesBtn = dialog.findViewById(R.id.btnYes) as TextView
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

    /* Finish the race and update the information
    * hides the start-stop button */
    fun completeRaceAndUpdateInfo(time: String) {
        isActive = false
        timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
        raceInfo.time = time
        raceInfo.isFinished = true
        binding.imageStartStop.visibility = View.GONE
        Prefs(requireContext()).updateRace(raceInfo, raceInfo.name)
    }

    /* Opens dialog to rename the race */
    private fun editRaceDialog(raceName: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_race_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val editText = dialog.findViewById(R.id.body) as EditText
        val yesBtn: TextView = dialog.findViewById(R.id.btnYes)
        val noBtn: TextView = dialog.findViewById(R.id.btnNo)

        editText.hint = raceName

        noBtn.setOnClickListener { dialog.dismiss() }

        yesBtn.setOnClickListener {
            raceInfo.name = editText.text.toString()
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                editText.text.toString()
            Prefs(requireContext()).updateRace(raceInfo, raceName)
            dialog.dismiss()
        }
        dialog.show()
    }

    /* Display menu icons on toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.race_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /* Handles toolbar icons clicks */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                println("clicked")
                editRaceDialog(raceInfo.name)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* Filter the ships list by name */
    private fun filterList(letter: CharSequence) {
        var originalList = shipList
        val newList = originalList.filter { ship -> ship.name.contains(letter, ignoreCase = true) }
        originalList = newList as MutableList<ShipData>
        val adapter = RaceShipListAdapter(originalList)
        adapter.setOnShipTime(this)
        binding.recyclerRaceShipList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRaceShipList.adapter = adapter
    }
}