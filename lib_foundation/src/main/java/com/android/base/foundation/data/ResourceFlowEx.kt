package com.android.base.foundation.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T : Any?> MutableSharedFlow<Resource<T>>.emitLoading() {
    emit(Resource.loading())
}

suspend fun <T : Any?> MutableSharedFlow<Resource<T>>.emitError(error: Throwable) {
    emit(Resource.error(error))
}

suspend fun <T : Any?> MutableSharedFlow<Resource<T>>.emitData(t: T? = null) {
    val resource = if (t == null) {
        Resource.noData()
    } else {
        Resource.success(t)
    }
    emit(resource)
}

suspend fun <T : Any?> MutableStateFlow<Resource<T>>.emitLoading() {
    emit(Resource.loading())
}

suspend fun <T : Any?> MutableStateFlow<Resource<T>>.emitError(error: Throwable) {
    emit(Resource.error(error))
}

suspend fun <T : Any?> MutableStateFlow<Resource<T>>.emitData(t: T? = null) {
    val resource = if (t == null) {
        Resource.noData()
    } else {
        Resource.success(t)
    }
    emit(resource)
}

fun <T : Any?> MutableStateFlow<Resource<T>>.setLoading() {
    value = Resource.loading()
}

fun <T : Any?> MutableStateFlow<Resource<T>>.setError(error: Throwable) {
    value = Resource.error(error)
}

fun <T : Any?> MutableStateFlow<Resource<T>>.setData(t: T? = null) {
    val resource = if (t == null) {
        Resource.noData()
    } else {
        Resource.success(t)
    }
    value = resource
}
