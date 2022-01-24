package com.android.base.adapter.recycler

import android.content.Context
import com.android.base.foundation.adapter.DataManager

/**
 * @see [MultiTypeAdapter](https://github.com/drakeet/MultiType)
 */
open class MultiTypeAdapter : com.drakeet.multitype.MultiTypeAdapter, DataManager<Any> {

    val context: Context

    private var dataManager: RecyclerDataManagerImpl<Any>

    @Suppress("LeakingThis")
    constructor(context: Context) : super() {
        this.context = context
        val items = mutableListOf<Any>()
        dataManager = RecyclerDataManagerImpl(items, this)
        super.items = items
    }

    @Suppress("LeakingThis")
    constructor(context: Context, items: List<Any>) : super() {
        this.context = context
        val mutableList = items.toMutableList()
        dataManager = RecyclerDataManagerImpl(mutableList, this)
        super.items = mutableList
    }

    override fun add(element: Any) {
        dataManager.add(element)
    }

    override fun addAt(location: Int, element: Any) {
        dataManager.addAt(location, element)
    }

    override fun addItems(elements: List<Any>) {
        dataManager.addItems(elements)
    }

    override fun addItemsChecked(elements: List<Any>) {
        dataManager.addItemsChecked(elements)
    }

    override fun addItemsAt(location: Int, elements: List<Any>) {
        dataManager.addItemsAt(location, elements)
    }

    override fun replace(oldElement: Any, newElement: Any) {
        dataManager.replace(oldElement, newElement)
    }

    override fun replaceAt(index: Int, element: Any) {
        dataManager.replaceAt(index, element)
    }

    override fun replaceAll(elements: List<Any>) {
        dataManager.replaceAll(elements)
    }

    override fun setDataSource(newDataSource: MutableList<Any>) {
        super.items = newDataSource
        dataManager.setDataSource(newDataSource)
    }

    override fun remove(element: Any): Boolean {
        return dataManager.remove(element)
    }

    override fun removeAt(index: Int) {
        dataManager.removeAt(index)
    }

    override fun removeItems(elements: List<Any>) {
        dataManager.removeItems(elements)
    }

    override fun removeItems(elements: List<Any>, isSuccessive: Boolean) {
        dataManager.removeItems(elements, isSuccessive)
    }

    override fun getItem(position: Int): Any? {
        return dataManager.getItem(position)
    }

    override operator fun contains(element: Any): Boolean {
        return dataManager.contains(element)
    }

    override fun clear() {
        dataManager.clear()
    }

    override fun indexItem(element: Any): Int {
        return dataManager.indexItem(element)
    }

    override fun notifyElementChanged(element: Any) {
        dataManager.notifyElementChanged(element)
    }

    override fun isEmpty(): Boolean {
        return dataManager.isEmpty()
    }

    override fun getList(): List<Any> {
        return dataManager.getList()
    }

    override fun getDataSize(): Int {
        return dataManager.getDataSize()
    }

    override var items: List<Any>
        get() = super.items
        set(value) {
            if (value is MutableList) {
                super.items = value
                dataManager.setDataSource(value)
            } else {
                throw IllegalArgumentException("only support MutableList")
            }
        }

    override fun removeIf(filter: (Any) -> Boolean) {
        dataManager.removeIf(filter)
    }

    override fun swipePosition(fromPosition: Int, toPosition: Int) {
        dataManager.swipePosition(fromPosition, toPosition)
    }

}