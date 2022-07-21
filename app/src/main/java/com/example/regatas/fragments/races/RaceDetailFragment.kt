package com.example.regatas.fragments.races

import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.regatas.R
import com.example.regatas.`interface`.RaceAddShipInterface
import com.example.regatas.`interface`.RaceShipListInterface
import com.example.regatas.adapters.races.RaceAddShipAdapter
import com.example.regatas.adapters.raceshiplist.RaceShipListAdapter
import com.example.regatas.data.RaceData
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentRaceDetailBinding
import com.example.regatas.prefs.Prefs
import com.example.regatas.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import kotlin.math.absoluteValue


class RaceDetailFragment : Fragment(), RaceShipListInterface, RaceAddShipInterface {

    lateinit var binding: FragmentRaceDetailBinding
    private lateinit var raceInfo: RaceData
    private var isActive = false
    private lateinit var timer: Chronometer
    private lateinit var startStop: ImageView
    private var timeWhenStopped: Long = 0
    var shipList: MutableList<ShipData> = mutableListOf()
    private var dnfShipList: MutableList<ShipData> = mutableListOf()
    var dnsShipList: MutableList<ShipData> = mutableListOf()
    private var shipsFinishedCount = 0
    private lateinit var addShipInterface: RaceAddShipInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        binding = FragmentRaceDetailBinding.inflate(inflater, container, false)

        val startStopBtn = binding.imageStartStop
        timer = binding.chronoRaceTime
        startStop = binding.imageStartStop

        startStopBtn.setOnClickListener { chronoController() }

        /* gets data from previous fragment */
        getArgs()

        if (!raceInfo.isFinished) recyclersInit(shipList, binding.recyclerRaceShipList)
        else shipListSelector()

        if (binding.imageStartStop.visibility == View.VISIBLE) {
//            centerTimer()
        }

//        /* ONChange listener for editText */
//        binding.editSearchShip.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                filterList(p0!!)
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })

        /* format to 00:00:00*/
        chronoMeterFormat()

        /* Toolbar title */
        (requireActivity() as AppCompatActivity).supportActionBar?.title = raceInfo.name

        return binding.root
    }

    /* Get the args from the clicked race on the previous fragment */
    private fun getArgs() {
        val args = this.arguments?.getString("raceInfo")
        val type = object : TypeToken<RaceData>() {}.type
        raceInfo = Gson().fromJson(args, type)

        if (raceInfo.time != null) {
            binding.chronoRaceTime.text = raceInfo.time
            binding.imageStartStop.visibility = View.GONE
        }

        val ships = raceInfo.shipsList
        if (ships != null) shipList = ships
    }

    /* start stop chrono timer */
    private fun chronoController() {
        if (!isActive) startTimer()
        else finishRaceDialog(timer.text.toString())
    }

    private fun startTimer() {
        startStop.setImageResource(R.drawable.ic_stopstop)
        timer.base = SystemClock.elapsedRealtime() + timeWhenStopped
        timer.start()
        isActive = true
    }

    private fun recyclersInit(ships: MutableList<ShipData>, recyclerView: RecyclerView) {
        if (ships.size > 0) {
            if (raceInfo.isFinished) {
                ships.sortWith(compareBy({ !it.isFinished }, { it.time }))
            } else {
                ships.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            }
            val adapter = RaceShipListAdapter(ships)
            adapter.setOnShipTime(this)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
    }

    /* Assigns the current time to the clicked ships, and set isFinished to true.
    * if all ships are isFinished, ends the race */
    override fun onShipStopped(pos: Int) {
        if (!isActive) return
        if (raceInfo.isFinished) Toast.makeText(
            requireContext(),
            "Race finished",
            Toast.LENGTH_SHORT
        ).show()
        else {
            shipList[pos].time = timer.text.toString()
            shipList[pos].isFinished = true
            shipsFinishedCount++
            binding.recyclerRaceShipList.adapter?.notifyItemRemoved(pos)
            binding.recyclerRaceShipList.adapter?.notifyItemRangeChanged(pos, shipList.size)
            shipList.add(shipList.removeAt(pos))
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
            dnfDialog()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            timer.start()
            dialog.dismiss()
        }
        dialog.show()
    }

    /* shows pop up with ships that did not finish the race
    * you can either add them to DNS or DNF */
    private fun dnfDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_dnf_ships)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val dnfRecyclerView = dialog.findViewById(R.id.recylerDNS) as RecyclerView

        dnfShips(dnfRecyclerView)
        val yesBtn = dialog.findViewById(R.id.btnYes) as TextView
        val noBtn = dialog.findViewById(R.id.btnNo) as TextView
        yesBtn.setOnClickListener {
            raceInfo.isFinished = true
            shipListSelector()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            timer.start()
            dialog.dismiss()
        }
        dialog.show()
    }
    /*TODO finish the race and save info*/

    /* divide the ship list into finished, dns, dnf*/
    private fun shipListSelector() {
        val finishedShips = shipList.filter { it.isFinished } as MutableList
        binding.recyclerRaceShipList.adapter = RaceShipListAdapter(finishedShips)
        if (raceInfo.isFinished) {
            recyclersInit(finishedShips, binding.recyclerRaceShipList)
        }

        val dnfShips = dnfShipList.filter { !it.dns } as MutableList
        if (dnfShips.size > 0) {
            recyclersInit(dnfShips, binding.recyclerDNF)
            binding.dnfConstraint.visibility = View.VISIBLE
        }

        if (dnsShipList.size > 0) {
            recyclersInit(dnsShipList, binding.recyclerDNS)
            binding.dnsConstraint.visibility = View.VISIBLE
        }
    }

    /* creates the list with ships that did not finish */
    private fun dnfShips(recyclerView: RecyclerView) {
        dnfShipList = shipList.filter { !it.isFinished } as MutableList
        for (ship in dnfShipList) {
            ship.isSelected = false
        }
        val adapter = RaceAddShipAdapter(dnfShipList)
        adapter.setOnShipSelected(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    /* Finish the race and update the information
    * hides the start-stop button */
    private fun completeRaceAndUpdateInfo(time: String) {
        isActive = false
        timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
        raceInfo.time = time
        raceInfo.isFinished = true
        binding.imageStartStop.visibility = View.GONE
        centerTimer()
        for (ship in shipList) {
            if (!ship.isFinished) {
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
    private fun centerTimer() {
        val constraintLayout = binding.mainConstraint
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
        dialog.setContentView(R.layout.dialog_edit_race)
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

    private fun deleteRace() {
        val storageRaces = Prefs(requireContext()).getRacesFromStorage()
        val racesIterator = storageRaces?.iterator()
        while (racesIterator!!.hasNext()) {
            val race = racesIterator.next()
            if (race == raceInfo) {
                racesIterator.remove()
                Prefs(requireContext()).saveRace(storageRaces)
                Toast.makeText(
                    requireContext(),
                    "Carrera ${raceInfo.name} eliminada",
                    Toast.LENGTH_SHORT
                ).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_raceDetailFragment_to_raceFragment)
                Navigation.findNavController(requireView()).popBackStack()
                break
            }
        }
    }


    /* checks whether race is finished or not to display export icon */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val export = menu.findItem(R.id.export)
        export.isVisible = raceInfo.isFinished
        val edit = menu.findItem(R.id.edit)
        edit.isVisible = !raceInfo.isFinished
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
                parseRaceInfoAndNavigate(R.id.action_raceDetailFragment_to_editRaceFragment)
                true
            }
            R.id.export -> {
                saveData()
                true
            }
            R.id.delete -> {
                deleteRace()
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
        var root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        if you want to create a sub -dir
//        root = File(root, "SubDir");
//        root.mkdir();

//        select the name for your file
        root = File(root, "${raceInfo.name}.csv")

        try {
            val fout = FileOutputStream(root)
            fout.write(csv.toByteArray())
            Toast.makeText(
                requireContext(),
                "Carrera exportada como ${raceInfo.name}",
                Toast.LENGTH_SHORT
            ).show()
            fout.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
//          if file not found create it and call the method again
            var bool = false
            try {
                // try to create the file
                bool = root.createNewFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (bool) {
                // call the method again
                saveData()
            } else {
                throw  IllegalStateException("Failed to create image file")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // goes to edit race with current race info
    private fun parseRaceInfoAndNavigate(toFragmentAction: Int) {
        val newName = raceInfo.name
        val newDate = raceInfo.date
        val parsedData = Gson().toJson(raceInfo, object : TypeToken<RaceData>() {}.type)
        val bundle = Bundle()
        bundle.putString("raceName", newName)
        bundle.putString("raceDate", newDate)
        bundle.putString("raceInfo", parsedData)
        val fragment = RaceDetailFragment()
        fragment.arguments = bundle
        Utils.navigateTo(requireActivity(), toFragmentAction, bundle)
    }

    // set format to HH:MM:SS
    private fun chronoMeterFormat() {
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

    override fun selectShip(ship: ShipData, pos: Int) {
        if (ship.isSelected) {
            dnsShipList.remove(ship)
            ship.dns = false
        } else {
            dnsShipList.add(ship)
            ship.dns = true
        }
        ship.isSelected = !ship.isSelected
    }
}