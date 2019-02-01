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

class InventoryAdapter internal constructor(context: Context, val itemInteractor: ItemInteractor) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<Item>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val cost: TextView = itemView.findViewById(R.id.item_cost)
        val weight: TextView = itemView.findViewById(R.id.item_weight)
        val isEquipped: ImageView = itemView.findViewById(R.id.item_equipped)
    }

    interface ItemInteractor {
        fun onLongPress(item: Item): Boolean
        fun onPress(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.item_inventory, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.cost.text = inflater.context.getString(R.string.money, items[position].cost)
        holder.weight.text = inflater.context.getString(R.string.weight, items[position].weight)
        if (items[position].isEquipped) {
            holder.isEquipped.visibility = View.VISIBLE
        } else {
            holder.isEquipped.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener { itemInteractor.onPress(items[position]) }
        holder.itemView.setOnLongClickListener {
            itemInteractor.onLongPress(items[position])
        }

    }

    internal fun setItems(items: List<Item>) {
        this.items = items
        this.notifyDataSetChanged()
    }
}