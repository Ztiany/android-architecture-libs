package com.android.base.utils.common

fun <E> List<E>?.ifNotEmpty(action: List<E>.() -> Unit) {
    if (!this.isNullOrEmpty()) {
        action(this)
    }
}

fun <E> MutableList<E>.removeFirstWhen(filter: (E) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
            break
        }
    }
    return removed
}

fun <E> MutableList<E>.removeAllWhen(firstMatchOnly: Boolean = false, filter: (E) -> Boolean): Int {
    var removed = 0
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed++
        }
    }
    return removed
}


inline fun <T> List<T>.findFrom(startIndex: Int = 0, predicate: (T) -> Boolean): T? {
    var element: T
    for (i in startIndex until size) {
        element = get(i)
        if (predicate(element)) return element
    }

    return null
}