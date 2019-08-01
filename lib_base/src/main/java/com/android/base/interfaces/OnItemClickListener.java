package com.android.base.interfaces;

import android.support.annotation.NonNull;
import android.view.View;


public abstract class OnItemClickListener<T> implements View.OnClickListener {

    @Override
    @SuppressWarnings("unchecked")
    public final void onClick(View v) {
        Object tag = v.getTag();
        if (tag == null) {
            throw new NullPointerException("OnItemClickListener --> no tag found");
        }
        onClick(v, (T) tag);
    }

    public abstract void onClick(@NonNull View view, @NonNull T t);

}