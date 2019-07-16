package com.android.base.app.aac;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.WeakHashMap;

import timber.log.Timber;

public class SingleLiveData<T> extends MediatorLiveData<T> {

    private int mVersion = 0;

    private final Map<Observer<T>, Observer<T>> cache = new WeakHashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        super.observe(owner, getOrNewObserver(observer, mVersion));
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        super.observeForever(getOrNewObserver(observer, mVersion));
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void removeObserver(@NonNull Observer<T> observer) {
        Observer<T> wrapper = cache.remove(observer);
        Timber.d("removeObserver() called with: observer = [" + observer + "], wrapper = [" + wrapper + "]");
        if (wrapper != null) {
            super.removeObserver(wrapper);
        }
    }

    private Observer<T> getOrNewObserver(@NonNull Observer<T> observer, int observerVersion) {
        Observer<T> wrapper = cache.get(observer);

        if (wrapper == null) {
            wrapper = t -> {
                if (observerVersion < mVersion) {
                    observer.onChanged(t);
                }
            };
            cache.put(observer, wrapper);
        }

        Timber.d("getOrNewObserver() called with: observer = [" + observer + "], observerVersion = [" + observerVersion + "], wrapper = [" + wrapper + "]");

        return wrapper;
    }

}