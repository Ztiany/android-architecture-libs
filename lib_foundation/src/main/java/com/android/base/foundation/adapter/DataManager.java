package com.android.base.foundation.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     注意数据源引用的替换，只有setDataSource方法会把elements替换掉之前的数据源引用，其他方法都是基于现有数据集合做删除与添加操作。
 * </pre>
 *
 * @author Ztiany
 * Date : 2016-09-12 11:33
 * Email: 1169654504@qq.com
 */
public interface DataManager<T> {

    void add(T element);

    void addAt(int location, T element);

    void addItems(List<T> elements);

    /**
     * 添加元素前会使用equals方法进行比较。
     *
     * @param elements 元素
     */
    void addItemsChecked(List<T> elements);

    void addItemsAt(int location, List<T> elements);

    void replace(T oldElement, T newElement);

    void replaceAt(int index, T element);

    /**
     * 清除之前集合中的数据，然后把elements添加到之前的集合中，不会使用elements作为数据源
     *
     * @param elements 元素
     */
    void replaceAll(List<T> elements);

    /**
     * 此方法会使用 newDataSource 替换掉之前的数据源，而不对之前的数据源做任何操作。
     *
     * @param newDataSource        新的数据集
     * @param notifyDataSetChanged 是否调用adapter的notifyDataSetChanged方法
     */
    void setDataSource(List<T> newDataSource, boolean notifyDataSetChanged);

    void remove(T element);

    void removeAt(int index);

    void removeItems(List<T> elements);

    void removeItems(List<T> elements, boolean isSuccessive);

    @Nullable
    T getItem(int position);

    @NonNull
    List<T> getItems();

    int getDataSize();

    boolean contains(T element);

    boolean isEmpty();

    void clear();

    /**
     * @param t element
     * @return -1 if not contains this element
     */
    int indexItem(T t);

}
