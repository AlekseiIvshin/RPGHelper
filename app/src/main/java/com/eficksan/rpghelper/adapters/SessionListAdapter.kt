package com.eficksan.rpghelper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item

class SessionListAdapter internal constructor(context: Context, itemInteractor: ItemInteractor<GameSession>) :
    BaseSelectingAdapter<SessionListAdapter.ViewHolder, GameSession>(context, itemInteractor) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sessionName: TextView = itemView.findViewById(R.id.session_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.item_session, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.sessionName.text = items[position].name
        holder.itemView.setOnClickListener { itemInteractor.onPress(items[position]) }
        holder.itemView.setOnLongClickListener { itemInteractor.onLongPress(items[position]) }
    }
}