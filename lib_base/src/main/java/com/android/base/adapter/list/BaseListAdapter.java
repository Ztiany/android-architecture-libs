package com.android.base.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.base.R;
import com.android.base.adapter.DataManager;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * absListView通用的Adapter,注意：只有setDataSource才能替换原有数据源的引用。
 *
 * @param <T> 数据模型
 * @author Ztiany
 */
@SuppressWarnings("unused")
public abstract class BaseListAdapter<T, VH extends ViewHolder> extends BaseAdapter implements DataManager<T> {

    protected final Context mContext;
    private final static int ITEM_ID = R.id.base_item_tag_view_id;
    private DataManager<T> mDataManager;
    private final LayoutInflater mLayoutInflater;

    public BaseListAdapter(@NonNull Context context) {
        this(context, null);
    }

    @SuppressWarnings("all")
    public BaseListAdapter(Context context, List<T> data) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDataManager = new ListDataManagerImpl<>(data, this);
    }

    @Override
    public int getCount() {
        return getDataSize();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        VH viewHolder;
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = onCreateViewHolder(mLayoutInflater, parent, type);
            viewHolder.mItemView.setTag(ITEM_ID, viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag(ITEM_ID);
        }
        viewHolder.setPosition(position);
        viewHolder.setType(type);
        T item = getItem(position);
        onBindData(viewHolder, item);
        return viewHolder.mItemView;
    }

    @SuppressWarnings("all")
    protected abstract void onBindData(@NonNull VH viewHolder, T item);

    @NonNull
    protected abstract VH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent, int type);

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
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
    public void setDataSource(List<T> elements, boolean notifyDataSetChanged) {
        mDataManager.setDataSource(elements, notifyDataSetChanged);
    }

    @Override
    public int getItemPosition(T t) {
        return mDataManager.getItemPosition(t);
    }

    @Override
    public void clear() {
        mDataManager.clear();
    }

    @Override
    public List<T> getItems() {
        return mDataManager.getItems();
    }
}
