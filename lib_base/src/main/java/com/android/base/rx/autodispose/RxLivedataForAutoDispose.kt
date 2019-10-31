package com.android.base.rx.autodispose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.data.State
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.*

fun <T> ObservableSubscribeProxy<T>.toResourceLiveData(): LiveData<State<T>> {
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

fun <T> ObservableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<State<T>> {
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

fun <T> FlowableSubscribeProxy<T>.toResourceLiveData(): LiveData<State<T>> {
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

fun <T> FlowableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<State<T>> {
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

fun CompletableSubscribeProxy.toResourceLiveData(): LiveData<State<Any>> {
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

fun <T> ObservableSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> FlowableSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> SingleSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> MaybeSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}