package com.android.base.adapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.adapter.recycler.RecyclerDataManagerImpl
import com.android.base.foundation.adapter.DataManager

class SimpleFragmentStateAdapter<T>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    items: List<T> = emptyList(),
    private val itemIdProvider: ((T) -> Long)? = null,
    private val fragmentFactory: (Int, T) -> Fragment,
) : FragmentStateAdapter(fragmentManager, lifecycle), DataManager<T> {

    constructor(
        host: FragmentActivity,
        items: List<T> = emptyList(),
        itemIdProvider: ((T) -> Long)? = null,
        fragmentFactory: (Int, T) -> Fragment
    ) : this(host.supportFragmentManager, host.lifecycle, items, itemIdProvider, fragmentFactory)

    constructor(
        host: Fragment,
        items: List<T> = emptyList(),
        itemIdProvider: ((T) -> Long)? = null,
        fragmentFactory: (Int, T) -> Fragment
    ) : this(host.childFragmentManager, host.lifecycle, items, itemIdProvider, fragmentFactory)

    override fun createFragment(position: Int): Fragment {
        val item = getItem(position) ?: throw NullPointerException()
        return fragmentFactory(position, item)
    }

    override fun getItemId(position: Int): Long {
        return itemIdProvider?.invoke(getItem(position) ?: throw NullPointerException()) ?: super.getItemId(position)
    }

    @Suppress("LeakingThis")
    private val dataManager: RecyclerDataManagerImpl<T> = RecyclerDataManagerImpl(items.toMutableList(), this)

    override fun getItemCount(): Int {
        return getDataSize()
    }

    ///////////////////////////////////////////////////////////////////////////
    // DataManager
    ///////////////////////////////////////////////////////////////////////////

    override fun add(element: T) {
        dataManager.add(element)
    }

    override fun addAt(location: Int, element: T) {
        dataManager.addAt(location, element)
    }

    override fun addItems(elements: List<T>) {
        dataManager.addItems(elements)
    }

    override fun addItemsChecked(elements: List<T>) {
        dataManager.addItemsChecked(elements)
    }

    override fun addItemsAt(location: Int, elements: List<T>) {
        dataManager.addItemsAt(location, elements)
    }

    override fun replace(oldElement: T, newElement: T) {
        dataManager.replace(oldElement, newElement)
    }

    override fun replaceAt(index: Int, element: T) {
        dataManager.replaceAt(index, element)
    }

    override fun replaceAll(elements: List<T>) {
        dataManager.replaceAll(elements)
    }

    override fun remove(element: T): Boolean {
        return dataManager.remove(element)
    }

    override fun removeItems(elements: List<T>) {
        dataManager.removeItems(elements)
    }

    override fun removeItems(elements: List<T>, isSuccessive: Boolean) {
        dataManager.removeItems(elements, isSuccessive)
    }

    override fun removeAt(index: Int) {
        dataManager.removeAt(index)
    }

    override fun getItem(position: Int): T? {
        return dataManager.getItem(position)
    }

    override fun contains(element: T): Boolean {
        return dataManager.contains(element)
    }

    override fun clear() {
        dataManager.clear()
    }

    override fun indexItem(element: T): Int {
        return dataManager.indexItem(element)
    }

    override fun setDataSource(newDataSource: MutableList<T>) {
        dataManager.setDataSource(newDataSource)
    }

    override fun notifyElementChanged(element: T) {
        dataManager.notifyElementChanged(element)
    }

    override fun isEmpty(): Boolean {
        return dataManager.isEmpty()
    }

    override fun getList(): List<T> {
        return dataManager.getList()
    }

    override fun getDataSize(): Int {
        return dataManager.getDataSize()
    }

    override fun removeIf(filter: (T) -> Boolean) {
        dataManager.removeIf(filter)
    }

    override fun swipePosition(fromPosition: Int, toPosition: Int) {
        dataManager.swipePosition(fromPosition, toPosition)
    }

}