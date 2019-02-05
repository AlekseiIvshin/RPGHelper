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

class SessionListAdapter internal constructor(val context: Context, val itemInteractor: ItemInteractor) :
    RecyclerView.Adapter<SessionListAdapter.ViewHolder>() {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<GameSession>()
    private  var selectedItems = emptyList<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sessionName: TextView = itemView.findViewById(R.id.session_name)
    }

    interface ItemInteractor {
        fun onLongPress(item: GameSession): Boolean
        fun onPress(session: GameSession)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.item_session, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sessionName.text = items[position].name
        holder.itemView.setOnClickListener { itemInteractor.onPress(items[position]) }
        holder.itemView.setOnLongClickListener { itemInteractor.onLongPress(items[position]) }

        if (this.selectedItems.contains(items[position].uid)){
            holder.itemView.setBackgroundColor(context.resources.getColor(android.R.color.holo_green_dark, context.theme))
        } else {
            holder.itemView.setBackgroundColor(context.resources.getColor(android.R.color.white, context.theme))
        }
    }

    internal fun setSessions(sessions: List<GameSession>) {
        this.items= sessions
        this.notifyDataSetChanged()
    }

    internal fun setSelectedItems(selectedItems: List<String>) {
        this.selectedItems = selectedItems
        this.notifyDataSetChanged()
    }
}