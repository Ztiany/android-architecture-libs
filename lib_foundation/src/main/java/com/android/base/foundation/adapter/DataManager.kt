package com.android.base.foundation.adapter

/**
 * @author Ztiany
 * Date : 2016-09-12 11:33
 */
interface DataManager<T> {

    fun add(element: T)

    fun addAt(location: Int, element: T)

    fun addItems(elements: List<T>)

    /**
     * 添加元素前会使用equals方法进行比较。
     *
     * @param elements 元素
     */
    fun addItemsChecked(elements: List<T>)

    fun addItemsAt(location: Int, elements: List<T>)

    fun replace(oldElement: T, newElement: T)

    fun replaceAt(index: Int, element: T)

    /**
     * 清除之前集合中的数据，然后把elements添加到之前的集合中，不会使用elements作为数据源
     *
     * @param elements 元素
     */
    fun replaceAll(elements: List<T>)

    /**
     * 此方法会使用 newDataSource 替换掉之前的数据源，而不对之前的数据源做任何操作。
     *
     * @param newDataSource        新的数据集
     */
    fun setDataSource(newDataSource: MutableList<T>)

    fun swipePosition(fromPosition: Int, toPosition: Int)

    fun remove(element: T): Boolean

    fun removeIf(filter: (T) -> Boolean)

    fun removeAt(index: Int)

    fun removeItems(elements: List<T>)

    fun removeItems(elements: List<T>, isSuccessive: Boolean)

    fun getItem(position: Int): T?

    operator fun contains(element: T): Boolean

    fun clear()

    /**
     * @param element element
     * @return -1 if not contains this element
     */
    fun indexItem(element: T): Int

    fun notifyElementChanged(element: T)

    fun isEmpty(): Boolean

    fun getList(): List<T>

    fun getDataSize(): Int

}