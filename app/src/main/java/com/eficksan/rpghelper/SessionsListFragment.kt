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
import com.eficksan.rpghelper.viewmodels.SessionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SessionsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sessions_list, container, false)
        view.findViewById<FloatingActionButton>(R.id.create_game_session)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.create_session, null))

        activity?.let { act ->
            SessionViewModel(activity!!.application).allSessions.observe(
                this,
                Observer { it -> view.findViewById<TextView>(R.id.label).text = "Games count" + it.size })
        }
        return view
    }
}
