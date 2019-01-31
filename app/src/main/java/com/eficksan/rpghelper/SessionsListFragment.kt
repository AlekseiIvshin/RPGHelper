package com.eficksan.rpghelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.adapters.SessionListAdapter
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.viewmodels.SessionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SessionsListFragment : Fragment(), SessionListAdapter.ItemInteractor {

    private lateinit var viewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sessions_list, container, false)
        viewModel = SessionViewModel(activity!!.application)
        view.findViewById<FloatingActionButton>(R.id.create_game_session)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.create_session, null))

        val list = view.findViewById<RecyclerView>(R.id.sessions_list)
        val adapter = SessionListAdapter(view.context, this)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(view.context)

        activity?.let { act ->
            viewModel.allSessions.observe(
                this,
                Observer { adapter.setSessions(it) })
        }


        return view
    }

    override fun delete(session: GameSession) {
        viewModel.delete(session)
    }

}