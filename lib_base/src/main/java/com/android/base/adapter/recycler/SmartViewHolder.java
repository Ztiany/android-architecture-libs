package com.android.base.adapter.recycler;

import android.view.View;

import com.android.base.adapter.ItemHelper;


public class SmartViewHolder extends ViewHolder {

    private final ItemHelper mHelper;

    public SmartViewHolder(View itemView) {
        super(itemView);
        mHelper = new ItemHelper(itemView);
    }

    public ItemHelper helper() {
        return mHelper;
    }

}