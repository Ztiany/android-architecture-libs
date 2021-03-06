package com.android.base.adapter.recycler

import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

/**
 * refer [Paging With Header](http://www.cezcb.com/2018/08/24/PagingWithHeader) and [Android官方架构组件Paging-Ex:为分页列表添加Header和Footer](https://juejin.im/post/6844903814189826062)
 */
internal class AdapterDataObserverProxy(private val adapterDataObserver: AdapterDataObserver, private val headerCount: Int) : AdapterDataObserver() {

    override fun onChanged() {
        adapterDataObserver.onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeInserted(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeRemoved(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount)
    }

}