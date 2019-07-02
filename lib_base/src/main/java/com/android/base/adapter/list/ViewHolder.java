package com.android.base.adapter.list;

import android.view.View;

public class ViewHolder {

    @SuppressWarnings("all")
    protected final View mItemView;
    private int mPosition;
    private int mType;

    public ViewHolder(View itemView) {
        mItemView = itemView;
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
}
