package com.eficksan.rpghelper.screens


import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.viewmodels.InventoryItemViewModel
import com.eficksan.rpghelper.viewmodels.SessionViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemDetailsFragment : Fragment() {
    private var itemUid: String? = null

    private lateinit var bottomBar: BottomAppBar
    private lateinit var viewModel: InventoryItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemUid = it.getString("item_uid")
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_details, container, false)
        viewModel = InventoryItemViewModel(activity!!.application, itemUid!!)

        viewModel.item.observe(this, Observer {
            view.findViewById<TextView>(R.id.item_name).text = it.name
            view.findViewById<TextView>(R.id.item_weight).text = getString(R.string.weight, it.weight)
            view.findViewById<TextView>(R.id.item_cost).text = getString(R.string.money, it.cost)
            view.findViewById<TextView>(R.id.item_description).text = it.description
            if (it.isEquipped) {
                view.findViewById<ImageView>(R.id.item_equipped).visibility = View.VISIBLE
            } else {
                view.findViewById<ImageView>(R.id.item_equipped).visibility = View.INVISIBLE
            }
        })

        bottomBar = view.findViewById(R.id.bottom_bar)
        (activity as AppCompatActivity).setSupportActionBar(bottomBar)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val data = Bundle()
            data.putString("item_uid", itemUid)
            view.findNavController().navigate(R.id.add_inventory_item, data)
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_item_details, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> {
                view?.let {
                    it.findNavController().popBackStack()
                    viewModel.deleteCurrent()
                }
                return true
            }
            R.id.equip -> {
                viewModel.equipOrTakeOffCurrent()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
