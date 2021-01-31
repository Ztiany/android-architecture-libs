package com.android.base.adapter.list;

import android.view.View;

import com.android.base.adapter.ItemHelper;

public class ViewHolder {

    protected final View mItemView;
    private int mPosition;
    private int mType;
    private final ItemHelper mHelper;

    public ViewHolder(View itemView) {
        mItemView = itemView;
        mHelper = new ItemHelper(itemView);
    }

    public int getPosition() {
        return mPosition;
    }

    void setPosition(int position) {
        mPosition = position;
    }

    public int getType() {
        return mType;
    }

    void setType(int type) {
        mType = type;
    }

    public View getItemView() {
        return mItemView;
    }

    public ItemHelper helper() {
        return mHelper;
    }

}
