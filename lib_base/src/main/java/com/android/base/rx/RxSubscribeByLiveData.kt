package com.android.base.rx

import androidx.lifecycle.MutableLiveData
import com.android.base.data.State
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


fun <T> Observable<T>.subscribeByLiveData(liveData: MutableLiveData<State<T>>) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(it))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T, R> Observable<T>.subscribeByLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(map(it)))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T> Observable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<State<T>>) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(it.orElse(null)))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T, R> Observable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                val value = map(it.orElse(null))
                liveData.postValue(State.success(value))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T> Flowable<T>.subscribeByLiveData(liveData: MutableLiveData<State<T>>) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(it))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T, R> Flowable<T>.subscribeByLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(map(it)))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T> Flowable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<State<T>>) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(it.orElse(null)))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T, R> Flowable<Optional<T>>.subscribeOptionalByLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                val value = map(it.orElse(null))
                liveData.postValue(State.success(value))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun Completable.subscribeByLiveData(liveData: MutableLiveData<State<Any>>) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success())
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}

fun <T> Completable.subscribeByLiveData(liveData: MutableLiveData<State<T>>, provider: () -> T) {
    liveData.postValue(State.loading())
    this.subscribe(
            {
                liveData.postValue(State.success(provider()))
            },
            {
                liveData.postValue(State.error(it))
            }
    )
}