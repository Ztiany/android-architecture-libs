package com.android.base.rxjava.autodispose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.data.Resource
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.*

fun <T> ObservableSubscribeProxy<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
            {
                mutableLiveData.postValue(Resource.success(it))
            },
            {
                mutableLiveData.postValue(Resource.error(it))
            }
    )
    return mutableLiveData
}

fun <T> ObservableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
            {
                mutableLiveData.postValue(Resource.success(it.orElse(null)))
            },
            {
                mutableLiveData.postValue(Resource.error(it))
            }
    )
    return mutableLiveData
}

fun <T> FlowableSubscribeProxy<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
            {
                mutableLiveData.postValue(Resource.success(it))
            },
            {
                mutableLiveData.postValue(Resource.error(it))
            }
    )
    return mutableLiveData
}

fun <T> FlowableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
            {
                mutableLiveData.postValue(Resource.success(it.orElse(null)))
            },
            {
                mutableLiveData.postValue(Resource.error(it))
            }
    )
    return mutableLiveData
}

fun CompletableSubscribeProxy.toResourceLiveData(): LiveData<Resource<Any>> {
    val mutableLiveData = MutableLiveData<Resource<Any>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
            {
                mutableLiveData.postValue(Resource.success())
            },
            {
                mutableLiveData.postValue(Resource.error(it))
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