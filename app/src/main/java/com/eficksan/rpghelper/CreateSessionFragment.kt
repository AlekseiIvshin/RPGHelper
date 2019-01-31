package com.eficksan.rpghelper


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.viewmodels.SessionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class CreateSessionFragment : Fragment() {

    private lateinit var btnApplyChanges: FloatingActionButton
    private lateinit var etSessionName: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_session, container, false)
        btnApplyChanges = view.findViewById(R.id.apply_changes)
        etSessionName = view.findViewById(R.id.session_name)

        etSessionName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnApplyChanges.isEnabled = s != null && !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        btnApplyChanges.setOnClickListener {
            activity?.let { act ->
                SessionViewModel(activity!!.application).insert(
                    GameSession(
                        UUID.randomUUID().toString(),
                        etSessionName.text.toString()
                    )
                )
            }
        }
        return view
    }

}
