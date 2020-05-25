package com.android.sdk.social.wechat;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import timber.log.Timber;

public class SingleLiveData<T> extends MediatorLiveData<T> {

    private int mVersion = 0;

    private final List<WeakReference<ObserverWrapper<? super T>>> mWrapperObserverList = new ArrayList<>();

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, getOrNewObserver(observer, mVersion));
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(getOrNewObserver(observer, mVersion));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeObserver(@NonNull Observer<? super T> observer) {
        if (observer instanceof ObserverWrapper) {
            super.removeObserver(observer);
            removeWrapper((ObserverWrapper) observer);
            Timber.d("removeObserver() called with: observer = wrapper = [" + observer + "]");
        } else {
            ObserverWrapper<? super T> wrapper = findWrapper(observer);
            Timber.d("removeObserver() called with: observer = [" + observer + "], wrapper = [" + wrapper + "]");
            super.removeObserver(wrapper);
            removeWrapper(wrapper);
        }
    }

    private void removeWrapper(ObserverWrapper observer) {
        ListIterator<WeakReference<ObserverWrapper<? super T>>> iterator = mWrapperObserverList.listIterator();
        while (iterator.hasNext()) {
            WeakReference<ObserverWrapper<? super T>> next = iterator.next();
            ObserverWrapper<? super T> item = next.get();
            if (item == observer) {
                iterator.remove();
                break;
            }
        }
    }

    private ObserverWrapper<? super T> findWrapper(Observer<? super T> observer) {
        ListIterator<WeakReference<ObserverWrapper<? super T>>> iterator = mWrapperObserverList.listIterator();

        ObserverWrapper<? super T> target = null;

        while (iterator.hasNext()) {
            WeakReference<ObserverWrapper<? super T>> next = iterator.next();
            ObserverWrapper<? super T> item = next.get();
            if (item == null) {
                iterator.remove();
            } else if (item.mOrigin == observer) {
                target = item;
            }
        }

        return target;
    }

    private Observer<? super T> getOrNewObserver(@NonNull Observer<? super T> observer, int observerVersion) {
        ObserverWrapper<? super T> wrapper = findWrapper(observer);

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