package com.android.base.rxjava.livedata

import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.data.Resource
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


fun <T> Observable<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Observable<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> Observable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Observable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun <T> Flowable<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Flowable<T>.subscribeByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> Flowable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Flowable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun Completable.subscribeByLiveData(liveData: MutableLiveData<Resource<Any>>) {
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

fun <T> Completable.subscribeByLiveData(liveData: MutableLiveData<Resource<T>>, provider: () -> T) {
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