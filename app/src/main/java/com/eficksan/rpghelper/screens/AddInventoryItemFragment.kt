package com.eficksan.rpghelper.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.findNavController
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddInventoryItemFragment : Fragment() {
    private lateinit var sessionUid: String
    private lateinit var viewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionUid = it.getString("session_uid")!!
            viewModel = InventoryViewModel(activity!!.application, sessionUid)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_inventory_item, container, false)

        view.findViewById<FloatingActionButton>(R.id.apply_changes).setOnClickListener {
            viewModel.insert(
                Item(
                    UUID.randomUUID().toString(),
                    view.findViewById<EditText>(R.id.item_name).text.toString(),
                    view.findViewById<EditText>(R.id.item_description).text.toString(),
                    view.findViewById<EditText>(R.id.item_weight).text.toString().toFloat(),
                    view.findViewById<EditText>(R.id.item_cost).text.toString().toInt(),
                    sessionUid,
                    false
                )
            )

            view.findNavController().popBackStack()
        }

        return view
    }
}
