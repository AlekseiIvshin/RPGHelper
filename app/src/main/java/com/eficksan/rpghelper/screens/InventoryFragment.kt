package com.eficksan.rpghelper.screens

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.adapters.InventoryAdapter
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventoryFragment : Fragment(), InventoryAdapter.ItemInteractor {

    private var sessionUid: String? = null

    private lateinit var viewModel: InventoryViewModel

    private lateinit var bottomBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionUid = it.getString("session_uid")
            viewModel = InventoryViewModel(activity!!.application, sessionUid!!)
        }
        setHasOptionsMenu(true)
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

        bottomBar = view.findViewById(R.id.bottom_bar)

        (activity as AppCompatActivity).setSupportActionBar(bottomBar)

        val itemsList: RecyclerView = view.findViewById(R.id.items)
        val adapter = InventoryAdapter(view.context, this)
        itemsList.adapter = adapter
        itemsList.layoutManager = LinearLayoutManager(view.context)

        viewModel.allItems.observe(
            this,
            Observer { adapter.setItems(it) })
        viewModel.selectedItems.observe(this, Observer {
            activity?.invalidateOptionsMenu()
        })


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (inflater!= null && viewModel.selectedItems.value!=null && viewModel.selectedItems.value!!.isNotEmpty()) {
            if (viewModel.selectedItems.value!!.size == 1) {
                inflater.inflate(R.menu.menu_single_item_selected,menu)
            } else if (viewModel.selectedItems.value!!.size > 1) {
                inflater.inflate(R.menu.menu_multi_item_selected,menu)
            }
        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cancel_selection-> {
                viewModel.clearSelection()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLongPress(item: Item): Boolean {
        viewModel.selectItem(item)
        return true
    }

    override fun onPress(item: Item) {
        if (viewModel.mode.value == InventoryViewModel.MODE_SELECTION) {
            viewModel.selectItem(item)
        } else {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}
