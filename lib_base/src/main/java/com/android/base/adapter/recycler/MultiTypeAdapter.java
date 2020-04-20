package com.android.base.adapter.recycler;

import android.content.Context;

import com.android.base.foundation.adapter.DataManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @see <a href='https://github.com/drakeet/MultiType'>drakeet/MultiTypeAdapter</a>
 */
public class MultiTypeAdapter extends com.drakeet.multitype.MultiTypeAdapter implements DataManager<Object> {

    private final Context mContext;

    private RecyclerDataManagerImpl<Object> mRecyclerDataManager;

    public MultiTypeAdapter(Context context) {
        super();
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>();
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items) {
        super();
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>(items);
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items, int initialCapacity) {
        super(items, initialCapacity);
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>(items);
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    @SuppressWarnings("unused")
    public void notifyEntryChanged(Object entry) {
        int itemPosition = indexItem(entry);
        if (itemPosition != -1) {
            notifyItemChanged(itemPosition);
        }
    }

    @Override
    public void add(Object elem) {
        mRecyclerDataManager.add(elem);
    }

    @Override
    public void addAt(int location, Object elem) {
        mRecyclerDataManager.addAt(location, elem);
    }

    @Override
    public void addItems(List<Object> elements) {
        mRecyclerDataManager.addItems(elements);
    }

    @Override
    public void addItemsChecked(List<Object> elements) {
        mRecyclerDataManager.addItemsChecked(elements);
    }

    @Override
    public void addItemsAt(int location, List<Object> elements) {
        mRecyclerDataManager.addItemsAt(location, elements);
    }

    @Override
    public void replace(Object oldElem, Object newElem) {
        mRecyclerDataManager.replace(oldElem, newElem);
    }

    @Override
    public void replaceAt(int index, Object elem) {
        mRecyclerDataManager.replaceAt(index, elem);
    }

    @Override
    public void replaceAll(List<Object> elements) {
        mRecyclerDataManager.replaceAll(elements);
    }

    @Override
    public void setDataSource(@NonNull List<Object> newDataSource, boolean notifyDataSetChanged) {
        super.setItems(newDataSource);
        mRecyclerDataManager.setDataSource(newDataSource, notifyDataSetChanged);
    }

    @Override
    public void remove(Object elem) {
        mRecyclerDataManager.remove(elem);
    }

    @Override
    public void removeAt(int index) {
        mRecyclerDataManager.removeAt(index);
    }

    @Override
    public void removeItems(List<Object> elements) {
        mRecyclerDataManager.removeItems(elements);
    }

    @Override
    public void removeItems(List<Object> elements, boolean isSuccessive) {
        mRecyclerDataManager.removeItems(elements, isSuccessive);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mRecyclerDataManager.getItem(position);
    }

    @Override
    public int getDataSize() {
        return mRecyclerDataManager.getDataSize();
    }

    @Override
    public boolean contains(Object elem) {
        return mRecyclerDataManager.contains(elem);
    }

    @Override
    public boolean isEmpty() {
        return mRecyclerDataManager.isEmpty();
    }

    @Override
    public void clear() {
        mRecyclerDataManager.clear();
    }

    @NonNull
    @Override
    public List<Object> getItems() {
        return mRecyclerDataManager.getItems();
    }

    @Override
    public int indexItem(Object o) {
        return mRecyclerDataManager.indexItem(o);
    }

    @Override
    public void setItems(@NonNull List<?> items) {
        ArrayList<Object> objects = new ArrayList<>(items);
        super.setItems(objects);
        mRecyclerDataManager.setDataSource(objects, true);
    }

    public Context getContext() {
        return mContext;
    }

}