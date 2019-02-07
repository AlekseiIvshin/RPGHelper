package com.eficksan.rpghelper.screens

import android.app.Dialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.adapters.BaseSelectingAdapter
import com.eficksan.rpghelper.adapters.InventoryAdapter
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.eficksan.rpghelper.viewmodels.SelectableListViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventoryFragment : Fragment(), BaseSelectingAdapter.ItemInteractor<Item> {

    private var sessionUid: String? = null

    private lateinit var viewModel: InventoryViewModel

    private lateinit var money: TextView
    private lateinit var totalWeight: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sessionUid = it.getString("session_uid")
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.bottom_bar))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = InventoryViewModel(activity!!.application, sessionUid!!)

        view.findViewById<FloatingActionButton>(R.id.add_item).setOnClickListener {
            val data = Bundle()
            data.putString("session_uid", sessionUid)
            view.findNavController().navigate(R.id.add_inventory_item, data)
        }

        totalWeight = view.findViewById(R.id.total_weight)

        val itemsList: RecyclerView = view.findViewById(R.id.items)
        val adapter = InventoryAdapter(view.context, this)
        itemsList.adapter = adapter
        itemsList.layoutManager = LinearLayoutManager(view.context)

        money = view.findViewById(R.id.money)
        money.setOnClickListener {
            with(AlertDialog.Builder(context!!)) {
                setTitle(R.string.change_money_by)
                val input = EditText(context)
                input.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_SIGNED)
                setView(input)
                setPositiveButton(R.string.apply) { _, _ ->
                    run {
                        viewModel.updateMoney(input.text.toString().toInt())
                    }
                }
                show()
            }

        }

        viewModel.money.removeObservers(this)
        viewModel.money.observe(this, Observer { money.text = getString(R.string.money, it) })

        viewModel.allItems.removeObservers(this)
        viewModel.allItems.observe(
            this,
            Observer {
                adapter.updateItems(it)
                totalWeight.text = getString(R.string.weight, it.sumByDouble { item -> item.weight.toDouble() })
            })

        viewModel.selectedItems.removeObservers(this)
        viewModel.selectedItems.observe(this, Observer {
            activity?.invalidateOptionsMenu()
            adapter.updateSelectedItems(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (inflater != null && viewModel.selectedItems.value != null && viewModel.selectedItems.value!!.isNotEmpty()) {
            if (viewModel.selectedItems.value!!.size == 1) {
                inflater.inflate(R.menu.menu_single_item_selected, menu)
            } else if (viewModel.selectedItems.value!!.size > 1) {
                inflater.inflate(R.menu.menu_multi_item_selected, menu)
            }
        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cancel_selection -> {
                viewModel.clearSelection()
                return true
            }
            R.id.delete -> {
                viewModel.deleteSelected()
                viewModel.clearSelection()
                return true
            }
            R.id.equip -> {
                viewModel.equipOrTakeOffSelected()
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
        if (viewModel.mode.value == SelectableListViewModel.MODE_SELECTION) {
            viewModel.selectItem(item)
        } else {
            val data = Bundle()
            data.putString("item_uid", item.uid)
            view?.findNavController()?.navigate(R.id.itemDetailsFragment, data)
        }
    }

}
