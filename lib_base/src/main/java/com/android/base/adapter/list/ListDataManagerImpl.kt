package com.android.base.adapter.list

import android.widget.BaseAdapter
import com.android.base.foundation.adapter.DataManager
import com.android.base.utils.common.isEmpty
import java.util.*

internal class ListDataManagerImpl<T>(
    private var internalList: MutableList<T>,
    private val baseAdapter: BaseAdapter
) : DataManager<T> {

    override fun add(element: T) {
        internalList.add(element)
        notifyDataSetChanged()
    }

    override fun addAt(location: Int, element: T) {
        internalList.add(location, element)
        notifyDataSetChanged()
    }

    override fun addItems(elements: List<T>) {
        internalList.addAll(elements)
        notifyDataSetChanged()
    }

    override fun addItemsChecked(elements: List<T>) {
        if (isEmpty(elements)) {
            return
        }
        for (element in elements) {
            if (element == null) {
                continue
            }
            internalList.remove(element)
        }
        internalList.addAll(elements)
        notifyDataSetChanged()
    }

    override fun addItemsAt(location: Int, elements: List<T>) {
        internalList.addAll(location, elements)
        notifyDataSetChanged()
    }

    override fun replace(oldElement: T, newElement: T) {
        if (internalList.contains(oldElement)) {
            replaceAt(internalList.indexOf(oldElement), newElement)
        }
    }

    override fun replaceAt(index: Int, element: T) {
        if (internalList.size > index) {
            internalList[index] = element
            notifyDataSetChanged()
        }
    }

    override fun replaceAll(elements: List<T>) {
        internalList.clear()
        internalList.addAll(elements)
        notifyDataSetChanged()
    }

    override fun setDataSource(newDataSource: MutableList<T>) {
        internalList = newDataSource
        notifyDataSetChanged()
    }

    override fun remove(element: T): Boolean {
        if (internalList.contains(element)) {
            internalList.remove(element)
            notifyDataSetChanged()
            return true
        }
        return false
    }

    override fun removeItems(elements: List<T>) {
        if (internalList.containsAll(elements)) {
            internalList.removeAll(elements)
            notifyDataSetChanged()
        }
    }

    override fun removeItems(elements: List<T>, isSuccessive: Boolean) {
        removeItems(elements)
    }

    override fun removeAt(index: Int) {
        if (internalList.size > index) {
            internalList.removeAt(index)
            notifyDataSetChanged()
        }
    }

    override fun getItem(position: Int): T? {
        return if (internalList.size > position) {
            internalList[position]
        } else null
    }

    override fun contains(element: T): Boolean {
        return internalList.contains(element)
    }

    override fun clear() {
        if (internalList.isNotEmpty()) {
            internalList.clear()
            notifyDataSetChanged()
        }
    }

    override fun indexItem(element: T): Int {
        return getList().indexOf(element)
    }

    private fun notifyDataSetChanged() {
        baseAdapter.notifyDataSetChanged()
    }

    override fun notifyElementChanged(element: T) {
        baseAdapter.notifyDataSetChanged()
    }

    override fun isEmpty(): Boolean {
        return internalList.isEmpty()
    }

    override fun getList(): List<T> {
        return internalList
    }

    override fun getDataSize(): Int {
        return internalList.size
    }

    override fun removeIf(filter: (T) -> Boolean) {
        internalList.removeAll(filter)
        notifyDataSetChanged()
    }

    override fun swipePosition(fromPosition: Int, toPosition: Int) {
        val intRange = 0 until internalList.size
        if (fromPosition != toPosition && fromPosition in intRange && toPosition in intRange) {
            Collections.swap(internalList, fromPosition, toPosition)
            notifyDataSetChanged()
        }
    }

}