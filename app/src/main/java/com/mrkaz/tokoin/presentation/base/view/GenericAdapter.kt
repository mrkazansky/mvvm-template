package com.mrkaz.tokoin.presentation.base.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class GenericAdapter<T>(var items: MutableList<T>, var listener: ((T) -> Unit)?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemsFiltered: MutableList<T> = items

    init {
        itemsFiltered = mutableListOf()
    }

    fun updateItems(items : MutableList<T>){
        this.items = items
        this.itemsFiltered = items
        notifyDataSetChanged()
    }

    fun addItems(items : MutableList<T>){
        val position = this.items.size
        this.items.addAll(items)
        this.itemsFiltered.addAll(items)
        notifyItemRangeInserted(position, items.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false), viewType
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? Binder<T>)?.bind(itemsFiltered[position])
        holder.itemView.tag = itemsFiltered[position]
        listener?.let { callback ->
            holder.itemView.setOnClickListener {
                callback.invoke(items[position])
            }
        }
    }

    override fun getItemCount(): Int {

        return itemsFiltered.size
    }

    override fun getItemViewType(position: Int): Int {

        return getLayoutId(position, itemsFiltered[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }
}