package com.android.base.app.aac

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.base.data.Resource
import com.android.base.rx.subscribeIgnoreError
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.*


//-----------------------------------------------------------------------------------------

fun <T> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun <T> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R?) {
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

fun Completable.subscribeWithLiveData(liveData: MutableLiveData<Resource<Any>>) {
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

//-----------------------------------------------------------------------------------------

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

//-----------------------------------------------------------------------------------------

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