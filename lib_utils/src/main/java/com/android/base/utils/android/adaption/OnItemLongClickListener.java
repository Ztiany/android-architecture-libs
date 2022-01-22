package com.android.base.utils.android.adaption;

import android.view.View;


public abstract class OnItemLongClickListener<T> implements View.OnLongClickListener {

    @Override
    @SuppressWarnings("unchecked")
    public final boolean onLongClick(View v) {
        Object tag = v.getTag();
        if (tag == null) {
            throw new NullPointerException("OnItemLongClickListener --> no tag found");
        }
        return onClick(v, (T) tag);
    }

    public abstract boolean onClick(View view, T item);

}