package com.android.base.adapter.list;

import android.widget.BaseAdapter;

import com.android.base.adapter.DataManager;
import com.android.base.utils.common.Checker;

import java.util.ArrayList;
import java.util.List;


final class ListDataManagerImpl<T> implements DataManager<T> {

    private List<T> mData;
    private BaseAdapter mBaseAdapter;

    ListDataManagerImpl(List<T> tList, BaseAdapter adapter) {
        mData = tList;
        mBaseAdapter = adapter;
    }

    private void checkData() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
    }

    @Override
    public void add(T elem) {
        checkData();
        mData.add(elem);
        notifyDataSetChanged();
    }

    @Override
    public void addAt(int location, T elem) {
        checkData();
        mData.add(location, elem);
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<T> elements) {
        checkData();
        mData.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public void addItemsChecked(List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }
        if (mData == null) {
            addItems(elements);
            return;
        }
        for (T element : elements) {
            if (element == null) {
                continue;
            }
            mData.remove(element);
        }
        mData.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public void addItemsAt(int location, List<T> elements) {
        checkData();
        mData.addAll(location, elements);
        notifyDataSetChanged();
    }

    @Override
    public void replace(T oldElem, T newElem) {
        if (mData != null && mData.contains(oldElem)) {
            replaceAt(mData.indexOf(oldElem), newElem);
        }
    }

    @Override
    public void replaceAt(int index, T elem) {
        if (mData != null && mData.size() > index) {
            mData.set(index, elem);
            notifyDataSetChanged();
        }
    }

    @Override
    public void replaceAll(List<T> elements) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        if (elements != null) {
            mData.addAll(elements);
        }
        notifyDataSetChanged();
    }

    @Override
    public void setDataSource(List<T> newDataSource, boolean notifyDataSetChanged) {
        mData = newDataSource;
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(T elem) {
        if (mData != null && mData.contains(elem)) {
            mData.remove(elem);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeItems(List<T> elements) {
        if (mData != null && mData.containsAll(elements)) {
            mData.removeAll(elements);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeItems(List<T> elements, boolean isSuccessive) {
        removeItems(elements);
    }

    @Override
    public void removeAt(int index) {
        if (mData != null && mData.size() > index) {
            mData.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public T getItem(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public final int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean contains(T elem) {
        return mData != null && mData.contains(elem);
    }

    @Override
    public boolean isEmpty() {
        return mData == null || mData.size() == 0;
    }

    @Override
    public void clear() {
        if (mData != null && !mData.isEmpty()) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(T t) {
        List<T> items = getItems();
        if (items == null) {
            return -1;
        }
        return items.indexOf(t);
    }

    @Override
    public List<T> getItems() {
        return mData;
    }

    private void notifyDataSetChanged() {
        mBaseAdapter.notifyDataSetChanged();
    }

}
