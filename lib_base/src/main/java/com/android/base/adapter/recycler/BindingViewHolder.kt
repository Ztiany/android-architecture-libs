package com.android.base.adapter.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-06 02:50
 */
open class BindingViewHolder<VB : ViewBinding>(
    val vb: VB
) : RecyclerView.ViewHolder(vb.root) {

    fun withVB(action: VB.() -> Unit) {
        with(vb, action)
    }

}
