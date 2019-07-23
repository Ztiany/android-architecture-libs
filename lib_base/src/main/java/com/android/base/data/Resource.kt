package com.android.base.data

/**when in loading*/
inline fun <T> Resource<T>.onLoading(onLoading: () -> Unit): Resource<T> {
    if (this.isLoading) {
        onLoading()
    }
    return this
}

/**when error occurred*/
inline fun <T> Resource<T>.onError(onError: (error: Throwable) -> Unit): Resource<T> {
    if (this.isError) {
        onError(error())
    }
    return this
}

/**when no change*/
inline fun <T> Resource<T>.onNoChange(onNoChange: () -> Unit): Resource<T> {
    if (this.isNoChange) {
        onNoChange()
    }
    return this
}

/**when succeeded*/
inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this.isSuccess) {
        onSuccess(this.orElse(null))
    }
    return this
}

/**when succeeded and has data*/
inline fun <T> Resource<T>.onSuccessWithData(onSuccess: (data: T) -> Unit): Resource<T> {
    val t = this.get()
    if (this.isSuccess && t != null) {
        onSuccess(t)
    }
    return this
}

val <T> Resource<T>?.isFailed
    get() = this != null && this.isError

val <T> Resource<T>?.isSucceeded
    get() = this != null && this.isSuccess

val <T> Resource<T>?.inLoading
    get() = this != null && this.isLoading