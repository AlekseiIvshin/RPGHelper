package com.eficksan.rpghelper.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.eficksan.rpghelper.viewmodels.ItemUpdateViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateItemFragment : Fragment() {
    private var sessionUid: String? = null
    private var itemUid: String? = null
    private lateinit var viewModel: ItemUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionUid = it.getString("session_uid")
            itemUid = it.getString("item_uid")
        }
        viewModel = ItemUpdateViewModel(activity!!.application, itemUid, sessionUid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_inventory_item, container, false)

        val itemName = view.findViewById<EditText>(R.id.item_name)
        val itemDescription = view.findViewById<EditText>(R.id.item_description)
        val itemWeight = view.findViewById<EditText>(R.id.item_weight)
        val itemCost = view.findViewById<EditText>(R.id.item_cost)

        viewModel.item.observe(this, Observer{
            itemName.setText(it.name)
            itemDescription.setText(it.description)
            itemWeight.setText(it.weight.toString())
            itemCost.setText(it.cost.toString())
        })

        view.findViewById<FloatingActionButton>(R.id.apply_changes).setOnClickListener {
            viewModel.applyChanges(
                Item(
                    viewModel.item.value!!.uid,
                    itemName.text.toString(),
                    itemDescription.text.toString(),
                    itemWeight.text.toString().toFloat(),
                    itemCost.text.toString().toInt(),
                    viewModel.item.value!!.sessionUid,
                    false
                )
            )

            view.findNavController().popBackStack()
        }

        return view
    }
}
