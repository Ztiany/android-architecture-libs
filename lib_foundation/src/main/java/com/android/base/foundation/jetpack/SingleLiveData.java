package com.android.base.foundation.jetpack;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import timber.log.Timber;

public class SingleLiveData<T> extends MediatorLiveData<T> {

    private int mVersion = 0;

    private final List<ObserverWrapper<? super T>> mWrapperObserverList = new ArrayList<>();

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner,  getOrNewObserver(observer, mVersion));
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(getOrNewObserver(observer, mVersion));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        ObserverWrapper<? super T> wrapper = findWrapper(observer);
        super.removeObserver(wrapper);
        mWrapperObserverList.remove(wrapper);
    }

    private ObserverWrapper<? super T> getOrNewObserver(@NonNull Observer<? super T> observer, int observerVersion) {
        ObserverWrapper<? super T> wrapper = findWrapper(observer);

        if (wrapper == null) {
            wrapper = new ObserverWrapper<>(observerVersion, observer);
            mWrapperObserverList.add(wrapper);
        }

        return wrapper;
    }

    private ObserverWrapper<? super T> findWrapper(Observer<? super T> observer) {
        ListIterator<ObserverWrapper<? super T>> iterator = mWrapperObserverList.listIterator();

        ObserverWrapper<? super T> target = null;

        while (iterator.hasNext()) {
            ObserverWrapper<? super T> next = iterator.next();
            if (next.mOrigin == observer) {
                target = next;
                Timber.d("findWrapper next.mOrigin == observer");
                break;
            }
            if (next == observer) {
                Timber.d("findWrapper next == observer");
                target = next;
                break;
            }
        }

        return target;
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