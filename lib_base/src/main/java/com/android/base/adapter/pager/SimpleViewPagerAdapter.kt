package com.android.base.adapter.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class SimpleViewPagerAdapter<T, VB : ViewBinding>(data: List<T>) : ViewPagerAdapter<T, BindingViewHolder<VB>>(data) {

    override fun createViewHolder(container: ViewGroup): BindingViewHolder<VB> {
        return BindingViewHolder(provideViewBinding(LayoutInflater.from(container.context), container))
    }

    abstract fun provideViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

}

class BindingViewHolder<VB : ViewBinding>(vb: VB) : ViewPagerAdapter.ViewHolder(vb.root)