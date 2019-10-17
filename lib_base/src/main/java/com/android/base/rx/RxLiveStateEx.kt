package com.android.base.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.data.State
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


//-----------------------------------------------------------------------------------------
fun <T> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
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

fun <T> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
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

fun <T> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<State<R>>, map: (T) -> R) {
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

fun <T> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<T>>) {
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

fun <T, R> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<State<R>>, map: (T?) -> R?) {
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

fun Completable.subscribeWithLiveData(liveData: MutableLiveData<State<Any>>) {
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

fun <T> Completable.subscribeWithLiveData(liveData: MutableLiveData<State<T>>, provider: () -> T) {
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

//-----------------------------------------------------------------------------------------

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

//-----------------------------------------------------------------------------------------
