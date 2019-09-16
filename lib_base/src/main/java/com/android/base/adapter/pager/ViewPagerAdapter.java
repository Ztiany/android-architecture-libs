package com.android.base.adapter.pager;

import android.view.View;
import android.view.ViewGroup;

import com.android.base.R;
import com.android.base.adapter.pager.recycler.RecyclingPagerAdapter;
import com.android.base.utils.common.Checker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 如果使用可缩放的 View 作为 pager，可能不适合使用此Adapter
 *
 * @param <T>  数据
 * @param <VH> View Holder类型
 */
public abstract class ViewPagerAdapter<T, VH extends ViewPagerAdapter.ViewHolder> extends RecyclingPagerAdapter {

    private List<T> mData;

    public ViewPagerAdapter(@Nullable List<T> data) {
        mData = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup container) {
        VH viewHolder;
        if (convertView == null) {
            viewHolder = createViewHolder(container);
        } else {
            viewHolder = (VH) convertView.getTag(R.id.base_item_tag_view_id);
        }
        T item = getItem(position);
        onBindData(viewHolder, item);
        return viewHolder.itemView;
    }

    protected abstract VH createViewHolder(@NonNull ViewGroup container);

    protected abstract void onBindData(@NonNull VH viewHolder, @NonNull T item);

    @Override
    public int getCount() {
        return Checker.isEmpty(mData) ? 0 : mData.size();
    }

    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    public static class ViewHolder {

        public View itemView;

        public ViewHolder(@NonNull View itemView) {
            this.itemView = itemView;
            itemView.setTag(R.id.base_item_tag_view_id, this);
        }

    }

}
