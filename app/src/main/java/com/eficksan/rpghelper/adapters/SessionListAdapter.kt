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

class SessionListAdapter internal constructor(context: Context, val itemInteractor: ItemInteractor) :
    RecyclerView.Adapter<SessionListAdapter.ViewHolder>() {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<GameSession>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sessionName: TextView = itemView.findViewById(R.id.session_name)
        val delete: Button = itemView.findViewById(R.id.delete_session)
    }

    interface ItemInteractor {
        fun onPress(session: GameSession)
        fun onDelete(session: GameSession)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.item_session, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sessionName.text = items[position].name
        holder.delete.setOnClickListener { itemInteractor.onDelete(items[position]) }
        holder.itemView.setOnClickListener { itemInteractor.onPress(items[position]) }

    }

    internal fun setSessions(sessions: List<GameSession>) {
        this.items= sessions
        this.notifyDataSetChanged()
    }
}