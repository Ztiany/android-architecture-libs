package com.android.base.adapter.recycler;

import android.content.Context;
import android.view.ViewGroup;

import com.android.base.adapter.DataManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 的适配器
 * 注意：  只有setDataSource才能替换原有数据源的引用。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * date :    2015-05-11 22:38
 * email:    1169654504@qq.com
 */
public abstract class RecyclerAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> implements DataManager<T> {

    private RecyclerDataManagerImpl<T> mDataManager;

    @NonNull
    private final Context mContext;

    public RecyclerAdapter(@NonNull Context context, List<T> data) {
        mDataManager = new RecyclerDataManagerImpl<>(data, this);
        this.mContext = context;
    }

    public RecyclerAdapter(@NonNull Context context) {
        this(context, null);
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return getDataSize();
    }

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public abstract void onBindViewHolder(@NonNull VH viewHolder, int position);

    public void notifyEntryChanged(T t) {
        int itemPosition = indexItem(t);
        if (itemPosition != -1) {
            notifyItemChanged(itemPosition);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DataManager
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void add(T elem) {
        mDataManager.add(elem);
    }

    @Override
    public void addAt(int location, T elem) {
        mDataManager.addAt(location, elem);
    }

    @Override
    public void addItems(List<T> elements) {
        mDataManager.addItems(elements);
    }

    @Override
    public void addItemsChecked(List<T> elements) {
        mDataManager.addItemsChecked(elements);
    }

    @Override
    public void addItemsAt(int location, List<T> elements) {
        mDataManager.addItemsAt(location, elements);
    }

    @Override
    public void replace(T oldElem, T newElem) {
        mDataManager.replace(oldElem, newElem);
    }

    @Override
    public void replaceAt(int index, T elem) {
        mDataManager.replaceAt(index, elem);
    }

    @Override
    public void replaceAll(List<T> elements) {
        mDataManager.replaceAll(elements);
    }

    @Override
    public void remove(T elem) {
        mDataManager.remove(elem);
    }

    @Override
    public void removeItems(List<T> elements) {
        mDataManager.removeItems(elements);
    }

    @Override
    public void removeItems(List<T> elements, boolean isSuccessive) {
        mDataManager.removeItems(elements, isSuccessive);
    }

    @Override
    public void removeAt(int index) {
        mDataManager.removeAt(index);
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return mDataManager.getItem(position);
    }

    @Override
    public final int getDataSize() {
        return mDataManager.getDataSize();
    }

    @Override
    public boolean contains(T elem) {
        return mDataManager.contains(elem);
    }

    @Override
    public void clear() {
        mDataManager.clear();
    }

    @Override
    public void setDataSource(List<T> elements, boolean notifyDataSetChanged) {
        mDataManager.setDataSource(elements, notifyDataSetChanged);
    }

    @NonNull
    @Override
    public List<T> getItems() {
        return mDataManager.getItems();
    }

    protected final void setHeaderSize(HeaderSize headerSize) {
        mDataManager.setHeaderSize(headerSize);
    }

    @Override
    public boolean isEmpty() {
        return mDataManager.isEmpty();
    }

    @Override
    public int indexItem(T t) {
        return mDataManager.indexItem(t);
    }

}