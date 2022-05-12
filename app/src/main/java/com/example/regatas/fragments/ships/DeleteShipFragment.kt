package com.example.regatas.fragments.ships

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.regatas.R
import com.example.regatas.adapters.ships.DeleteShipAdapter
import com.example.regatas.data.ShipData
import com.example.regatas.databinding.FragmentDeleteShipBinding
import com.example.regatas.prefs.Prefs


class DeleteShipFragment : Fragment() {

    private lateinit var binding: FragmentDeleteShipBinding
    private var shipList = mutableListOf<ShipData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteShipBinding.inflate(inflater, container, false)

        getShips()

        recyclerView()

        binding.btnDeleteAll.setOnClickListener {
            showConfirmDialog()
        }
        return binding.root
    }

    private fun recyclerView() {
        binding.recyclerDeleteShip.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDeleteShip.adapter = DeleteShipAdapter(shipList)
    }

    private fun getShips() {
        val ships = Prefs(requireContext()).getShipsFromStorage()
        if (ships != null) shipList = ships
    }

    fun showConfirmDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirm_deleteall_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val yesBtn: TextView = dialog.findViewById(R.id.btnYes)
        val noBtn: TextView = dialog.findViewById(R.id.btnNo)


        noBtn.setOnClickListener { dialog.dismiss() }

        yesBtn.setOnClickListener {
            DeleteShipAdapter(shipList).removeAllShips(it.context)
            binding.recyclerDeleteShip.adapter?.notifyItemRangeRemoved(0, shipList.size)

            val navhostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host) as NavHostFragment
            val navController = navhostFragment.navController
            navController.navigate(R.id.action_deleteShipFragment_to_shipFragment)
            navController.popBackStack(R.id.deleteShipFragment, true)

            dialog.dismiss()
        }
        dialog.show()

    }
}



