package com.android.sdk.cache

import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable

inline fun <reified T> Storage.entity(key: String): T? {
    return this.getEntity(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.flowable(key: String): Flowable<T> {
    return this.flowable(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.optionalFlowable(key: String): Flowable<Optional<T>> {
    return this.optionalFlowable(key, object : TypeFlag<T>() {}.type)
}