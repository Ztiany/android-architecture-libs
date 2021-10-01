package com.android.base.rxjava.autodispose

import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.data.Resource
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.CompletableSubscribeProxy
import com.uber.autodispose.FlowableSubscribeProxy
import com.uber.autodispose.ObservableSubscribeProxy


fun <T> ObservableSubscribeProxy<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> ObservableSubscribeProxy<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun <T> FlowableSubscribeProxy<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> FlowableSubscribeProxy<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun CompletableSubscribeProxy.subscribeByLiveData(liveData: MutableLiveData<Resource<Any>>) {
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

fun <T> CompletableSubscribeProxy.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>, provider: () -> T) {
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
