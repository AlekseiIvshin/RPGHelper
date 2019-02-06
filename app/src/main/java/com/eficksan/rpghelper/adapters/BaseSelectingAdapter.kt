package com.eficksan.rpghelper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eficksan.rpghelper.R
import com.eficksan.rpghelper.models.BaseModel

abstract class BaseSelectingAdapter<VH: RecyclerView.ViewHolder, T: BaseModel> internal constructor(val context: Context, val itemInteractor: ItemInteractor<T>) :
    RecyclerView.Adapter<VH>() {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var items = emptyList<T>()
    private var selectedItems = emptyList<String>()

    interface ItemInteractor<T> {
        fun onLongPress(item: T): Boolean
        fun onPress(item: T)
    }

    protected fun isSelected(item: T)  = selectedItems.contains(item.uid)

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<T>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    fun updateSelectedItems(selectedItems: List<String>) {
        this.selectedItems = selectedItems
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        with(holder.itemView) {
            setOnClickListener { itemInteractor.onPress(items[position]) }
            setOnLongClickListener {
                itemInteractor.onLongPress(items[position])
            }

            if (isSelected(items[position])){
                setBackgroundColor(context.resources.getColor(android.R.color.holo_green_dark, context.theme))
            } else {
                setBackgroundColor(context.resources.getColor(android.R.color.white, context.theme))
            }
        }
    }

}