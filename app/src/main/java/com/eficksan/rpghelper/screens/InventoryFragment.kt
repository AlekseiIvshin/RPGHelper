package com.eficksan.rpghelper.screens

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.adapters.InventoryAdapter
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventoryFragment : Fragment(), InventoryAdapter.ItemInteractor {

    private var sessionUid: String? = null

    private lateinit var viewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionUid = it.getString("session_uid")
            viewModel = InventoryViewModel(activity!!.application, sessionUid!!)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        view.findViewById<FloatingActionButton>(R.id.add_item).setOnClickListener {
            val data = Bundle()
            data.putString("session_uid", sessionUid)
            view.findNavController().navigate(R.id.add_inventory_item, data)
        }

        val itemsList: RecyclerView = view.findViewById(R.id.items)
        val adapter = InventoryAdapter(view.context, this)
        itemsList.adapter = adapter
        itemsList.layoutManager = LinearLayoutManager(view.context)

        viewModel.allItems.observe(
            this,
            Observer { adapter.setItems(it) })

        return view
    }

    override fun onLongPress(item: Item): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPress(item: Item) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
