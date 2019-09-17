package com.android.base.adapter.recycler;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    public <V extends View> V findView(@IdRes int viewId) {
        return itemView.findViewById(viewId);
    }

    public ViewHolder setItemClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
        return this;
    }

}