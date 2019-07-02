package com.android.sdk.social.wechat;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

class SingleLiveData<T> extends MediatorLiveData<T> {

    private int mVersion = 0;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {

        final int observerVersion = mVersion;

        super.observe(owner, t -> {
            if (observerVersion < mVersion) {
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {

        final int observerVersion = mVersion;

        super.observeForever(t -> {
            if (observerVersion < mVersion) {
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

}