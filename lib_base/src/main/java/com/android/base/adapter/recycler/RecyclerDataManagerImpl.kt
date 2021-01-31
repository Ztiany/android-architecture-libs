package com.android.base.adapter.recycler

import androidx.recyclerview.widget.RecyclerView
import com.android.base.foundation.adapter.DataManager
import com.android.base.utils.common.isEmpty
import java.util.*

internal class RecyclerDataManagerImpl<T>(
        private var internalList: MutableList<T>,
        private val adapter: RecyclerView.Adapter<*>
) : DataManager<T> {

    private var headerSizeImpl: HeaderSize? = null

    override fun add(element: T) {
        addAt(getDataSize(), element)
    }

    override fun addAt(location: Int, element: T) {
        if (internalList.isEmpty()) {
            internalList.add(element)
            notifyItemInserted(headerSize)
        } else {
            val size = internalList.size
            val lastIndex = (if (location >= size) size else location) + headerSize
            internalList.add(location, element)
            notifyItemInserted(lastIndex)
        }
    }

    override fun addItems(elements: List<T>) {
        addItemsAt(getDataSize(), elements)
    }

    override fun addItemsChecked(elements: List<T>) {
        if (isEmpty(elements)) {
            return
        }
        var hasRemovedElements = false
        for (element in elements) {
            if (internalList.remove(element)) {
                if (!hasRemovedElements) {
                    hasRemovedElements = true
                }
            }
        }
        if (hasRemovedElements) {
            internalList.addAll(elements)
            notifyDataSetChanged()
        } else {
            addItems(elements)
        }
    }

    override fun addItemsAt(location: Int, elements: List<T>) {
        if (isEmpty(elements)) {
            return
        }
        if (internalList.isEmpty()) {
            internalList.addAll(elements)
            notifyItemRangeInserted(headerSize, elements.size)
        } else {
            val size = internalList.size
            val lastIndex = (if (location >= size) size else location) + headerSize
            internalList.addAll(location, elements)
            notifyItemRangeInserted(lastIndex, elements.size)
        }
    }

    override fun replace(oldElement: T, newElement: T) {
        if (internalList.contains(oldElement)) {
            replaceAt(internalList.indexOf(oldElement), newElement)
        }
    }

    override fun replaceAt(index: Int, element: T) {
        if (getDataSize() > index) {
            internalList[index] = element
            notifyItemChanged(index + headerSize)
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
        if (internalList.isEmpty()) {
            return false
        }
        if (internalList.contains(element)) {
            val index = internalList.indexOf(element) + headerSize
            internalList.remove(element)
            notifyItemRemoved(index)
            return true
        }
        return false
    }

    override fun removeAt(index: Int) {
        if (getDataSize() > index) {
            internalList.removeAt(index)
            notifyItemRemoved(index + headerSize)
        }
    }

    override fun removeItems(elements: List<T>) {
        if (!isEmpty(elements) && internalList.containsAll(elements)) {
            internalList.removeAll(elements)
            notifyDataSetChanged()
        }
    }

    override fun removeItems(elements: List<T>, isSuccessive: Boolean) {
        if (isEmpty(elements) || isEmpty(internalList)) {
            return
        }
        if (!isSuccessive) {
            removeItems(elements)
            return
        }
        val data = elements[0]
        val index = internalList.indexOf(data)
        if (index == -1) {
            removeItems(elements)
        } else {
            internalList.removeAll(elements)
            notifyItemRangeRemoved(index, elements.size)
        }
    }

    override fun getItem(position: Int): T? {
        val realPosition = position - headerSize
        return if (realPosition in 0 until getDataSize()) {
            internalList[realPosition]
        } else null
    }

    override fun contains(element: T): Boolean {
        return internalList.contains(element)
    }

    override fun clear() {
        internalList.clear()
        notifyDataSetChanged()
    }

    override fun indexItem(element: T): Int {
        val items = internalList
        return items.indexOf(element)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Size
    ///////////////////////////////////////////////////////////////////////////
    private val headerSize: Int
        get() = headerSizeImpl?.headerSize ?: 0

    ///////////////////////////////////////////////////////////////////////////
    // Adapter Call
    ///////////////////////////////////////////////////////////////////////////
    private fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    private fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    private fun notifyItemInserted(position: Int) {
        adapter.notifyItemInserted(position)
    }

    private fun notifyItemRangeInserted(position: Int, size: Int) {
        adapter.notifyItemRangeInserted(position, size)
    }

    private fun notifyItemRemoved(index: Int) {
        adapter.notifyItemRemoved(index)
    }

    private fun notifyItemRangeRemoved(index: Int, size: Int) {
        adapter.notifyItemRangeRemoved(index, size)
    }

    fun setHeaderSize(headerSize: HeaderSize?) {
        headerSizeImpl = headerSize
    }

    override fun isEmpty(): Boolean {
        return getDataSize() == 0
    }

    override fun getList(): List<T> {
        return internalList
    }

    override fun getDataSize(): Int {
        return internalList.size
    }

    override fun notifyElementChanged(element: T) {
        val position = indexItem(element)
        if (position != -1) {
            notifyItemChanged(position + headerSize)
        }
    }

    override fun removeIf(filter: (T) -> Boolean) {
        internalList.removeAll(filter)
        notifyDataSetChanged()
    }

    override fun swipePosition(fromPosition: Int, toPosition: Int) {
        val intRange = 0 until internalList.size
        if (fromPosition != toPosition && fromPosition in intRange && toPosition in intRange) {
            Collections.swap(internalList, fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
        }
    }

}