package com.android.base.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * A simple way to build a simple list. If you want to build a multi type list, perhaps you need to use [MultiTypeAdapter].
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-01-15 11:41
 */
abstract class SimpleRecyclerAdapter<T, VH : ViewHolder>(context: Context, data: List<T>? = null) : RecyclerAdapter<T, VH>(context, data) {

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = provideLayout(parent, viewType)
        val itemView = if (layout is Int) {
            mLayoutInflater.inflate(layout, parent, false)
        } else
            layout as View
        return provideViewHolder(itemView)
    }

    @Suppress("UNCHECKED_CAST")
    open fun provideViewHolder(itemView: View): VH {
        return (this::class.supertypes[0].arguments[1].type?.classifier as? KClass<VH>)?.primaryConstructor?.call(itemView)
                ?: throw IllegalArgumentException("need primaryConstructor, and arguments is (View)")
    }

    /**provide a layout id or a View*/
    abstract fun provideLayout(parent: ViewGroup, viewType: Int): Any

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (viewHolder.itemViewType == TYPE_ITEM) {
            bind(viewHolder, getItem(position))
        } else {
            bindOtherTypes(viewHolder, position)
        }
    }

    protected abstract fun bind(viewHolder: VH, item: T)

    protected open fun bindOtherTypes(viewHolder: ViewHolder, position: Int) {}

    companion object {
        protected const val TYPE_ITEM = 0
    }

}