package com.android.base.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.base.foundation.adapter.DataManager

/**
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * Date :    2015-05-11 22:38
 */
abstract class RecyclerAdapter<T, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    protected val context: Context,
    data: List<T> = emptyList()
) : RecyclerView.Adapter<VH>(), DataManager<T> {

    protected val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    @Suppress("LeakingThis")
    private val dataManager: RecyclerDataManagerImpl<T> = RecyclerDataManagerImpl(data.toMutableList(), this)

    override fun getItemCount(): Int {
        return getDataSize()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract override fun onBindViewHolder(viewHolder: VH, position: Int)

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

    protected fun setHeaderSize(headerSize: HeaderSize?) {
        dataManager.setHeaderSize(headerSize)
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