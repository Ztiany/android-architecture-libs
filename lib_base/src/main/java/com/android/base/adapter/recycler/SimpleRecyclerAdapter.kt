package com.android.base.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.kotlin.KtViewHolder

/**
 * A simple way to build a simple list. If you want to build a multi type list, perhaps you need to use [MultiTypeAdapter].
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-01-15 11:41
 */
abstract class SimpleRecyclerAdapter<T>(context: Context, data: List<T>? = null) : RecyclerAdapter<T, KtViewHolder>(context, data) {

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KtViewHolder {
        val layout = provideLayout(parent, viewType)
        val itemView = if (layout is Int) {
            mLayoutInflater.inflate(layout, parent, false)
        } else
            layout as View
        return KtViewHolder(itemView).apply {
            onViewHolderCreated(this)
        }
    }

    protected fun onViewHolderCreated(ktViewHolder: KtViewHolder) {

    }

    /**provide a layout id or a View*/
    abstract fun provideLayout(parent: ViewGroup, viewType: Int): Any

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onBindViewHolder(viewHolder: KtViewHolder, position: Int) {
        if (viewHolder.itemViewType == TYPE_ITEM) {
            bind(viewHolder, getItem(position))
        } else {
            bindOtherTypes(viewHolder, position)
        }
    }

    protected abstract fun bind(viewHolder: KtViewHolder, item: T)

    protected open fun bindOtherTypes(viewHolder: ViewHolder, position: Int) {}

    companion object {
        protected const val TYPE_ITEM = 0
    }

}