package com.eficksan.rpghelper.screens

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.adapters.SessionListAdapter
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item
import com.eficksan.rpghelper.viewmodels.InventoryViewModel
import com.eficksan.rpghelper.viewmodels.SessionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SessionsListFragment : Fragment(), SessionListAdapter.ItemInteractor {

    private lateinit var viewModel: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sessions_list, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.bottom_bar))

        viewModel = SessionViewModel(activity!!.application)
        view.findViewById<FloatingActionButton>(R.id.fab)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.create_session, null))

        val list = view.findViewById<RecyclerView>(R.id.sessions_list)
        val adapter = SessionListAdapter(view.context, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(view.context)

        activity?.let {
            viewModel.allSessions.observe(
                this,
                Observer { adapter.setSessions(it) })
        }
        viewModel.selectedItems.observe(this, Observer {
            activity?.invalidateOptionsMenu()
            adapter.setSelectedItems(it)
        })

        return view
    }

    override fun onLongPress(item: GameSession): Boolean {
        viewModel.selectItem(item)
        return true
    }

    override fun onPress(item: GameSession) {
        if (viewModel.mode.value == InventoryViewModel.MODE_SELECTION) {
            viewModel.selectItem(item)
        } else {
            view?.let {
                val data = Bundle()
                data.putString("session_uid", item.uid)
                it.findNavController().navigate(R.id.inventory, data)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (inflater != null && viewModel.selectedItems.value != null && viewModel.selectedItems.value!!.isNotEmpty()) {
            if (viewModel.selectedItems.value!!.size == 1) {
                inflater.inflate(R.menu.menu_single_session_selected, menu)
            } else if (viewModel.selectedItems.value!!.size > 1) {
                inflater.inflate(R.menu.menu_multi_session_selected, menu)
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
            R.id.edit -> {
                TODO("Open edit session screen")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
