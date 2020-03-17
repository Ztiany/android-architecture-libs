package com.android.base.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.data.State
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.*

fun <T> Observable<T>.toResourceLiveData(): LiveData<State<T>> {
    val mutableLiveData = MutableLiveData<State<T>>()
    mutableLiveData.value = State.loading()
    subscribe(
            {
                mutableLiveData.postValue(State.success(it))
            },
            {
                mutableLiveData.postValue(State.error(it))
            }
    )
    return mutableLiveData
}

fun <T> Observable<Optional<T>>.optionalToResourceLiveData(): LiveData<State<T>> {
    val mutableLiveData = MutableLiveData<State<T>>()
    mutableLiveData.value = State.loading()
    subscribe(
            {
                mutableLiveData.postValue(State.success(it.orElse(null)))
            },
            {
                mutableLiveData.postValue(State.error(it))
            }
    )
    return mutableLiveData
}

fun <T> Flowable<T>.toResourceLiveData(): LiveData<State<T>> {
    val mutableLiveData = MutableLiveData<State<T>>()
    mutableLiveData.value = State.loading()
    subscribe(
            {
                mutableLiveData.postValue(State.success(it))
            },
            {
                mutableLiveData.postValue(State.error(it))
            }
    )
    return mutableLiveData
}

fun <T> Flowable<Optional<T>>.optionalToResourceLiveData(): LiveData<State<T>> {
    val mutableLiveData = MutableLiveData<State<T>>()
    mutableLiveData.value = State.loading()
    subscribe(
            {
                mutableLiveData.postValue(State.success(it.orElse(null)))
            },
            {
                mutableLiveData.postValue(State.error(it))
            }
    )
    return mutableLiveData
}

fun Completable.toResourceLiveData(): LiveData<State<Any>> {
    val mutableLiveData = MutableLiveData<State<Any>>()
    mutableLiveData.value = State.loading()
    subscribe(
            {
                mutableLiveData.postValue(State.success())
            },
            {
                mutableLiveData.postValue(State.error(it))
            }
    )
    return mutableLiveData
}

fun <T> Observable<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Flowable<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Single<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Maybe<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}