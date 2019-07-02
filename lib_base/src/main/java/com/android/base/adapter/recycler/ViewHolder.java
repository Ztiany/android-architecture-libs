package com.android.base.adapter.recycler;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


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