package com.android.base.adapter.recycler;

import android.content.Context;
import android.view.View;

import com.android.base.adapter.ItemHelper;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private final ItemHelper mHelper;

    public ItemHelper helper() {
        return mHelper;
    }

    public ViewHolder(View itemView) {
        super(itemView);
        mHelper = new ItemHelper(itemView);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

}