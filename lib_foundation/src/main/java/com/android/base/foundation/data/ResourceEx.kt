package com.android.base.foundation.data

/**when in loading*/
inline fun <T> Resource<T>.onLoading(onLoading: () -> Unit): Resource<T> {
    if (this is Loading) {
        onLoading()
    }
    return this
}

/**when error occurred*/
inline fun <T> Resource<T>.onError(onError: (error: Throwable) -> Unit): Resource<T> {
    if (this is Error) {
        onError(error)
    }
    return this
}

/**when succeeded*/
inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this is NoData) {
        onSuccess(null)
    } else if (this is Data<T>) {
        onSuccess(value)
    }
    return this
}

/**when succeeded and has data*/
inline fun <T> Resource<T>.onData(onData: (data: T) -> Unit): Resource<T> {
    if (this is Data<T>) {
        onData(value)
    }
    return this
}

/**when succeeded*/
inline fun <T> Resource<T>.onNoData(onNoData: () -> Unit): Resource<T> {
    if (this is Data<T>) {
        onNoData()
    }
    return this
}