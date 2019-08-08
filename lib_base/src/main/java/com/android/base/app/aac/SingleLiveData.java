package com.android.base.app.aac;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import timber.log.Timber;

public class SingleLiveData<T> extends MediatorLiveData<T> {

    private int mVersion = 0;

    private final List<WeakReference<ObserverWrapper<T>>> mWrapperObserverList = new ArrayList<>();


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
        Observer<T> wrapper = findWrapper(observer);
        Timber.d("removeObserver() called with: observer = [" + observer + "], wrapper = [" + wrapper + "]");
        if (wrapper != null) {
            super.removeObserver(wrapper);
        }
    }

    private ObserverWrapper<T> findWrapper(Observer<T> observer) {
        ListIterator<WeakReference<ObserverWrapper<T>>> iterator = mWrapperObserverList.listIterator();

        ObserverWrapper<T> target = null;

        while (iterator.hasNext()) {
            WeakReference<ObserverWrapper<T>> next = iterator.next();
            ObserverWrapper<T> item = next.get();
            if (item == null) {
                iterator.remove();
            } else if (item.mOrigin == observer) {
                target =  item;
            }
        }

        return target;
    }

    private Observer<T> getOrNewObserver(@NonNull Observer<T> observer, int observerVersion) {
        ObserverWrapper<T> wrapper = findWrapper(observer);

        if (wrapper == null) {
            wrapper = new ObserverWrapper<>(observerVersion, observer);
            mWrapperObserverList.add(new WeakReference<>(wrapper));
        }

        Timber.d("getOrNewObserver() called with: observer = [" + observer + "], observerVersion = [" + observerVersion + "], wrapper = [" + wrapper + "]");

        return wrapper;
    }

    private class ObserverWrapper<E> implements Observer<E> {

        private final int mObserverVersion;

        private final Observer<E> mOrigin;

        private ObserverWrapper(int observerVersion, Observer<E> origin) {
            mObserverVersion = observerVersion;
            mOrigin = origin;
        }

        @Override
        public void onChanged(@Nullable E t) {
            if (mObserverVersion < mVersion && mOrigin != null) {
                mOrigin.onChanged(t);
            }
        }

    }

}