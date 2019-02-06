package com.eficksan.rpghelper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item

class InventoryAdapter internal constructor(context: Context, itemInteractor: ItemInteractor<Item>) :
    BaseSelectingAdapter<InventoryAdapter.ViewHolder, Item>(context, itemInteractor) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val cost: TextView = itemView.findViewById(R.id.item_cost)
        val weight: TextView = itemView.findViewById(R.id.item_weight)
        val isEquipped: ImageView = itemView.findViewById(R.id.item_equipped)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.item_inventory, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.name.text = items[position].name
        holder.cost.text = inflater.context.getString(R.string.money, items[position].cost)
        holder.weight.text = inflater.context.getString(R.string.weight, items[position].weight)
        if (items[position].isEquipped) {
            holder.isEquipped.visibility = View.VISIBLE
        } else {
            holder.isEquipped.visibility = View.INVISIBLE
        }
    }
}