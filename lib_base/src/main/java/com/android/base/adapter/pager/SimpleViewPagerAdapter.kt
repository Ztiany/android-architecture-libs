package com.android.base.adapter.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

/**
 * 如果使用可缩放的 View 作为 pager，可能不适合使用此Adapter
 *
 * @param <T>  数据
 * @param <VH> View Holder类型
</VH></T> */
abstract class SimpleViewPagerAdapter<T>(data: List<T>) : ViewPagerAdapter<T, KtViewHolder>(data) {

    override fun createViewHolder(container: ViewGroup): KtViewHolder {
        val layout = provideLayout(container)
        val itemView = if (layout is Int) {
            LayoutInflater.from(container.context).inflate(layout, container, false)
        } else {
            layout as View
        }
        return KtViewHolder(itemView)
    }

    /**provide a layout id or a View*/
    abstract fun provideLayout(parent: ViewGroup): Any

}

class KtViewHolder(itemView: View) : ViewPagerAdapter.ViewHolder(itemView), LayoutContainer {
    override val containerView: View = itemView
}