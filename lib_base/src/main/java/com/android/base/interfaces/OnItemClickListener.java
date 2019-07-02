package com.android.base.interfaces;

import android.view.View;

import timber.log.Timber;


public abstract class OnItemClickListener<T> implements View.OnClickListener {

    @Override
    @SuppressWarnings("unchecked")
    public final void onClick(View v) {
        Object tag = v.getTag();
        if (tag == null) {
            Timber.w("OnItemClickListener tag is null , view = " + v);
            return;
        }
        onClick(v, (T) tag);
    }

    public abstract void onClick(View view, T t);


}
