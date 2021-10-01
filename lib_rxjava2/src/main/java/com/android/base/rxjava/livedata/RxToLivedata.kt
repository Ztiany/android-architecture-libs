package com.android.base.rxjava.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.data.Resource
import com.android.base.rxjava.rxkit.subscribeIgnoreError
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.*

fun <T> Observable<T>.toResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> Observable<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> Flowable<T>.toResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> Flowable<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
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

fun Completable.toResourceLiveData(): LiveData<Resource<Any>> {
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