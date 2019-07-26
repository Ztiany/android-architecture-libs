package com.android.base.app.aac

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.base.data.Resource
import com.android.base.rx.subscribeIgnoreError
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.*


//-----------------------------------------------------------------------------------------

fun <T> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(it))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T, R> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(map(it)))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(it.orElse(null)))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T, R> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                val value = map(it.orElse(null))
                liveData.postValue(Resource.success(value))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(it))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T, R> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(map(it)))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(it.orElse(null)))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T, R> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                val value = map(it.orElse(null))
                liveData.postValue(Resource.success(value))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<Resource<Any>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success())
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

fun <T> CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>, provider: () -> T) {
    liveData.postValue(Resource.loading())
    this.subscribe(
            {
                liveData.postValue(Resource.success(provider()))
            },
            {
                liveData.postValue(Resource.error(it))
            }
    )
}

//-----------------------------------------------------------------------------------------

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

//-----------------------------------------------------------------------------------------

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