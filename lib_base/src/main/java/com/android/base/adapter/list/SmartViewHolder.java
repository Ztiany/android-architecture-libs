package com.android.base.adapter.list;

import android.view.View;

import com.android.base.adapter.ItemHelper;

@SuppressWarnings("unused")
public class SmartViewHolder extends ViewHolder {

    protected final ItemHelper mHelper;

    public SmartViewHolder(View itemView) {
        super(itemView);
        mHelper = new ItemHelper(itemView);
    }

    public ItemHelper helper() {
        return mHelper;
    }

}
