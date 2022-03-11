package com.android.base.adapter.recycler

import android.content.Context
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.android.base.foundation.adapter.DataManager
import com.android.base.utils.common.isEmpty
import java.util.*
import java.util.concurrent.Executor

/**
 * RecyclerView 的适配器。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * Date :    2015-05-11 22:38
 */
abstract class DiffRecyclerAdapter<T, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
        val context: Context,
        itemCallback: DiffUtil.ItemCallback<T>,
        executor: Executor? = null,
        private val headerCount: Int = 0
) : RecyclerView.Adapter<VH>(), DataManager<T> {

    private val asyncListDiffer: AsyncListDiffer<T>

    init {
        val builder = AsyncDifferConfig.Builder(itemCallback)
        if (executor != null) {
            builder.setBackgroundThreadExecutor(executor)
        }
        val differConfig = builder.build()
        asyncListDiffer = AsyncListDiffer(newListCallback(), differConfig)
    }

    private fun newListCallback() = AdapterListUpdateCallback(this)

    override fun registerAdapterDataObserver(observer: AdapterDataObserver) {
        if (headerCount != 0) {
            super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, headerCount))
        } else {
            super.registerAdapterDataObserver(observer)
        }
    }

    override fun getItemCount(): Int {
        return getDataSize()
    }

    override fun notifyElementChanged(element: T) {
        val position = indexItem(element)
        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    override fun add(element: T) {
        val newList = newList()
        newList.add(element)
        asyncListDiffer.submitList(newList)
    }

    override fun addAt(location: Int, element: T) {
        var position = location
        if (position > getDataSize()) {
            position = getDataSize()
        }
        val newList = newList()
        newList.add(position, element)
        asyncListDiffer.submitList(newList)
    }

    override fun addItems(elements: List<T>) {
        if (isEmpty(elements)) {
            return
        }
        val newList = newList()
        newList.addAll(elements)
        asyncListDiffer.submitList(newList)
    }

    override fun addItemsChecked(elements: List<T>) {
        if (isEmpty(elements)) {
            return
        }
        val newList = newList()
        for (element in elements) {
            if (element == null) {
                continue
            }
            newList.remove(element)
        }
        newList.addAll(elements)
        asyncListDiffer.submitList(newList)
    }

    override fun addItemsAt(location: Int, elements: List<T>) {
        var realLocation = location
        if (isEmpty(elements)) {
            return
        }
        val newList = newList()
        if (realLocation > newList.size) {
            realLocation = newList.size
        }
        newList.addAll(realLocation, elements)
        asyncListDiffer.submitList(newList)
    }

    override fun replace(oldElement: T, newElement: T) {
        if (!contains(oldElement)) {
            return
        }
        val newList = newList()
        newList[newList.indexOf(oldElement)] = newElement
        asyncListDiffer.submitList(newList)
    }

    override fun replaceAt(index: Int, element: T) {
        if (getDataSize() > index) {
            val newList = newList()
            newList[index] = element
            asyncListDiffer.submitList(newList)
        }
    }

    override fun replaceAll(elements: List<T>) {
        val newList: List<T> = ArrayList(elements)
        asyncListDiffer.submitList(newList)
    }

    override fun remove(element: T): Boolean {
        if (contains(element)) {
            val newList = newList()
            newList.remove(element)
            asyncListDiffer.submitList(newList)
            return true
        }
        return false
    }

    override fun removeItems(elements: List<T>) {
        if (isEmpty(elements) || isEmpty() || !getList().containsAll(elements)) {
            return
        }
        val newList = newList()
        newList.removeAll(elements)
        asyncListDiffer.submitList(newList)
    }

    override fun removeItems(elements: List<T>, isSuccessive: Boolean) {
        removeItems(elements)
    }

    override fun removeAt(index: Int) {
        if (getDataSize() > index) {
            val newList = newList()
            newList.removeAt(index)
            asyncListDiffer.submitList(newList)
        }
    }

    override fun getItem(position: Int): T? {
        return if (getDataSize() > position) getList()[position] else null
    }

    override fun contains(element: T): Boolean {
        return !isEmpty() && getList().contains(element)
    }

    override fun clear() {
        asyncListDiffer.submitList(null)
    }

    override fun indexItem(element: T): Int {
        return if (isEmpty()) -1 else getList().indexOf(element)
    }

    override fun removeIf(filter: (T) -> Boolean) {
        val newList = newList()
        newList.removeAll(filter)
        asyncListDiffer.submitList(newList)
    }

    private fun newList(): MutableList<T> {
        return mutableListOf<T>().apply {
            addAll(getList())
        }
    }

    override fun setDataSource(newDataSource: MutableList<T>) {
        asyncListDiffer.submitList(newDataSource)
    }

    override fun isEmpty(): Boolean {
        return getList().isEmpty()
    }

    override fun getList(): List<T> {
        return asyncListDiffer.currentList
    }

    override fun getDataSize(): Int {
        return getList().size
    }

    fun submitList(list: List<T>) {
        asyncListDiffer.submitList(list)
    }

    override fun swipePosition(fromPosition: Int, toPosition: Int) {
        val intRange = getList().indices
        if (fromPosition != toPosition && fromPosition in intRange && toPosition in intRange) {
            val newList = newList()
            Collections.swap(newList, fromPosition, toPosition)
            submitList(newList)
        }
    }

}