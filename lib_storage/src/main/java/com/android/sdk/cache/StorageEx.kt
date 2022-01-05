package com.android.sdk.cache

inline fun <reified T> Storage.getEntity(key: String): T? {
    return this.getEntity(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T : Any> Storage.getEntity(key: String, defaultValue: T): T {
    return this.getEntity(key, object : TypeFlag<T>() {}.type, defaultValue)
}