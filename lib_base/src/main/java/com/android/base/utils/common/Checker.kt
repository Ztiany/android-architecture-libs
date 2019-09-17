@file:JvmName("Checker")

package com.android.base.utils.common

fun isEmpty(data: Collection<*>?): Boolean {
    return data == null || data.isEmpty()
}

fun notEmpty(data: Collection<*>?): Boolean {
    return !isEmpty(data)
}

fun isNull(o: Any?): Boolean {
    return o == null
}

fun isEmpty(map: Map<*, *>?): Boolean {
    return map == null || map.isEmpty()
}

fun notEmpty(map: Map<*, *>): Boolean {
    return !isEmpty(map)
}

fun <T> isEmpty(t: Array<T>?): Boolean {
    return t == null || t.isEmpty()
}

fun <T> notEmpty(t: Array<T>): Boolean {
    return !isEmpty(t)
}

fun <T> requireNonNull(obj: T?): T {
    if (obj == null) {
        throw NullPointerException("requireNonNull failed")
    }
    return obj
}

fun <T> requireNonNull(obj: T?, message: String): T {
    if (obj == null) {
        throw NullPointerException("requireNonNull failed: %s".format(message))
    }
    return obj
}

fun nonNull(obj: Any?): Boolean {
    return obj != null
}
