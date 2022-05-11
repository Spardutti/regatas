package com.example.regatas.fragments.races

import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.`interface`.RaceShipListInterface
import com.example.regatas.data.RaceData
import com.example.regatas.adapters.raceshiplist.RaceShipListAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentRaceDetailBinding
import com.example.regatas.prefs.Prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*


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

        /* gets data from previous fragment */
        getArgs()

        initRecyclerView()

        if (binding.imageStartStop.visibility == View.GONE) {
            centerTimer()
        }

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

        /* format to 00:00:00*/
        chronoMeterFormat()

        /* Toolbar title */
        (requireActivity() as AppCompatActivity).supportActionBar?.title = raceInfo.name

        return binding.root
    }

    // set format to HH:MM:SS
    fun chronoMeterFormat() {
        timer.setOnChronometerTickListener {
            val time = SystemClock.elapsedRealtime() - timer.base
            val hours = (time / 3600000)
            val minutes = (time - hours * 3600000) / 60000
            val seconds = (time - hours * 3600000 - minutes * 60000) / 1000
            val timeString =
                "${if (hours < 10) "0$hours" else hours}:${if (minutes < 10) "0${minutes}" else minutes}:${if (seconds < 10) "0$seconds" else seconds}"

            timer.setText(timeString)
        }
        timer.setBase(SystemClock.elapsedRealtime())
        timer.text = ("00:00:00")
    }

    /* Get the args from the clicked race on the previous fragment */
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

    /* start stop chrono timer */
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
            shipList.sortWith(compareBy({ !it.isFinished }, { it.time }))
        } else {
            shipList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
        }
        val adapter = RaceShipListAdapter(shipList)
        adapter.setOnShipTime(this)
        binding.recyclerRaceShipList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRaceShipList.adapter = adapter
    }

    /* Assigns the current time to the clicked ships, and set isFinished to true.
    * if all ships are isFinished, ends the race */
    override fun onShipStopped(pos: Int) {
        if (raceInfo.isFinished) Toast.makeText(
            requireContext(),
            "La carrera ya ha terminado",
            Toast.LENGTH_SHORT
        ).show()
        else {
            shipList[pos].time = timer.text.toString()
            shipList[pos].isFinished = true
            shipsFinishedCount++
            shipList.add(shipList.removeAt(pos))
            binding.recyclerRaceShipList.adapter?.notifyItemRemoved(pos)
            binding.recyclerRaceShipList.adapter?.notifyItemInserted(shipList.size - 1)

            if (shipsFinishedCount == shipList.size) {
                completeRaceAndUpdateInfo(timer.text.toString())
                timer.stop()
                shipList.sortBy { it.time }
                binding.recyclerRaceShipList.adapter?.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Carrera terminada", Toast.LENGTH_SHORT).show()
            }
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
        centerTimer()
        for (ship in shipList) {
            if (ship.isFinished == false) {
                ship.time = "N/A"
                ship.isFinished = true
            }
        }
        shipList.sortBy { it.time }
        binding.recyclerRaceShipList.adapter?.notifyDataSetChanged()
        activity?.invalidateOptionsMenu()
        Prefs(requireContext()).updateRace(raceInfo, raceInfo.name)
    }


    /* Center time when start/stop is gone*/
    fun centerTimer() {
        val constraintLayout = binding.parent
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.chronoRaceTime,
            ConstraintSet.END,
            R.id.parent,
            ConstraintSet.END,
            0
        )
        constraintSet.connect(
            R.id.chronoRaceTime,
            ConstraintSet.START,
            R.id.parent,
            ConstraintSet.START,
            0
        )
        constraintSet.applyTo(constraintLayout)
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
            if (editText.text.isNotEmpty()) {
                raceInfo.name = editText.text.toString()
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    editText.text.toString()
                Prefs(requireContext()).updateRace(raceInfo, raceName)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    /* checks whether race is finished or not to display export icon */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val export = menu.findItem(R.id.export)
        export.isVisible = raceInfo.isFinished
        super.onPrepareOptionsMenu(menu)
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
                editRaceDialog(raceInfo.name)
                true
            }
            R.id.export -> {
                saveData()
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

    /* creates and export a csv file */
    private fun csvOf(
        headers: String,
        data: List<ShipData>,
        itemBuilder: (ShipData) -> List<String>
    ) = buildString {
//        append(headers.joinToString(",") { "\"$it\"" })
//        append("\n")
        append(headers)
        append("\n")
        data.forEach { item ->
            if (item.time == null) item.time = "N/A"
            append(itemBuilder(item).joinToString(",") { "\"$it\"" })
            append("\n")
        }
    }

    /* creates csv file and save it to download folders.*/
    private fun saveData() {
        val headers = "Nombre, Tiempo"
        val csv = csvOf(headers, raceInfo.shipsList!!) {
            listOf(it.name, it.time!!)
        }
        var root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        if you want to create a sub -dir
//        root = File(root, "SubDir");
//        root.mkdir();

//        select the name for your file
        root = File(root, "${raceInfo.name}.csv");

        try {
            val fout = FileOutputStream(root);
            fout.write(csv.toByteArray());
            Toast.makeText(
                requireContext(),
                "Carrera exportada como ${raceInfo.name}",
                Toast.LENGTH_SHORT
            ).show()
            fout.close();
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
//          if file not found create it and call the method again
            var bool = false;
            try {
                // try to create the file
                bool = root.createNewFile()
            } catch (ex: IOException) {
                ex.printStackTrace();
            }
            if (bool) {
                // call the method again
                saveData()
            } else {
                throw  IllegalStateException("Failed to create image file");
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }
}