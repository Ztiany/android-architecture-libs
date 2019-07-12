package com.android.base.data


inline fun <T> Resource<T>.onLoading(onLoading: () -> Unit): Resource<T> {
    if (this.isLoading) {
        onLoading()
    }
    return this
}

inline fun <T> Resource<T>.onError(onError: (error: Throwable) -> Unit): Resource<T> {
    if (this.isError) {
        onError(error())
    }
    return this
}

inline fun <T> Resource<T>.onNoChange(onNoChange: () -> Unit): Resource<T> {
    if (this.isNoChange) {
        onNoChange()
    }
    return this
}

/**success*/
inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this.isSuccess) {
        onSuccess(this.orElse(null))
    }
    return this
}

/**success with data*/
inline fun <T> Resource<T>.onSuccessWithData(onSuccess: (data: T) -> Unit): Resource<T> {
    if (this.isSuccess && this.hasData()) {
        onSuccess(this.data())
    }
    return this
}