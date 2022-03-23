package com.android.base.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.android.base.R
import com.android.base.foundation.adapter.DataManager

/**
 * AbsListView 通用的 Adapter 封装，注意：只有 [setDataSource] 才能替换原有数据源的引用。
 *
 * @param <T> 数据模型
 * @author Ztiany
 */
abstract class BaseListAdapter<T, VH : ViewHolder>(
    protected val context: Context,
    data: MutableList<T> = mutableListOf()
) : BaseAdapter(), DataManager<T> {

    private val dataManager = ListDataManagerImpl(data, this)

    private val layoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return getDataSize()
    }

    override fun isEmpty(): Boolean {
        return dataManager.isEmpty()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: VH
        val type = getItemViewType(position)
        if (convertView == null) {
            viewHolder = onCreateViewHolder(layoutInflater, parent, type)
            viewHolder.mItemView.setTag(ITEM_ID, viewHolder)
        } else {
            viewHolder = convertView.getTag(ITEM_ID) as VH
        }
        viewHolder.position = position
        viewHolder.type = type
        val item = getItem(position)
        onBindData(viewHolder, item)
        return viewHolder.mItemView
    }

    protected abstract fun onBindData(viewHolder: VH, item: T?)

    protected abstract fun onCreateViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup, type: Int): VH

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

    override fun indexItem(element: T): Int {
        return dataManager.indexItem(element)
    }

    override fun clear() {
        dataManager.clear()
    }

    override fun setDataSource(newDataSource: MutableList<T>) {
        dataManager.setDataSource(newDataSource)
    }

    override fun notifyElementChanged(element: T) {
        notifyDataSetChanged()
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

    companion object {
        private val ITEM_ID = R.id.base_item_tag_view_id
    }

}