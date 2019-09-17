package com.android.base.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple way to build a simple list. If you want to build a multi type list, perhaps you need to use [MultiTypeAdapter].
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-01-15 11:41
 */
abstract class SimpleRecyclerAdapter<T>(context: Context, data: List<T>? = null) : RecyclerAdapter<T, KtViewHolder>(context, data) {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KtViewHolder {
        val layout = provideLayout(parent, viewType)
        val itemView = if (layout is Int) {
            layoutInflater.inflate(layout, parent, false)
        } else
            layout as View
        return KtViewHolder(itemView).apply {
            onViewHolderCreated(this)
        }
    }

    protected open fun onViewHolderCreated(viewHolder: KtViewHolder) = Unit

    /**provide a layout id or a View*/
    abstract fun provideLayout(parent: ViewGroup, viewType: Int): Any

    override fun onBindViewHolder(viewHolder: KtViewHolder, position: Int) {
        bind(viewHolder, getItem(position))
    }

    protected abstract fun bind(viewHolder: KtViewHolder, item: T)

}