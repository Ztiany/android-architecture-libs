package com.android.base.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Ztiany
 * Date : 2017-09-13 15:33
 */
abstract class ItemViewBinder<T, VH : RecyclerView.ViewHolder> : com.drakeet.multitype.ItemViewBinder<T, VH>() {

    protected val dataManager: MultiTypeAdapter
        get() = adapter as MultiTypeAdapter

}

abstract class SimpleItemViewBinder<T, VB : ViewBinding> : ItemViewBinder<T, BindingViewHolder<VB>>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BindingViewHolder<VB> {
        return BindingViewHolder(provideViewBinding(inflater, parent)).apply {
            onViewHolderCreated(this)
        }
    }

    protected open fun onViewHolderCreated(viewHolder: BindingViewHolder<VB>) = Unit

    /**provide a layout id or a View*/
    abstract fun provideViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

}