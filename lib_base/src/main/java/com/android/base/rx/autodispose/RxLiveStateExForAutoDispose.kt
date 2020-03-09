package com.android.base.rx.autodispose

import androidx.lifecycle.MutableLiveData
import com.android.base.data.State
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.*


fun <T> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
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

fun <T> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
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

fun <T> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
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

fun <T> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
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

fun CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<State<Any>>) {
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

fun <T> CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<State<T>>, provider: () -> T) {
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
