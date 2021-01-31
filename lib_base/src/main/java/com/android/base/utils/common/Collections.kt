package com.android.base.utils.common

fun <E> List<E>?.toArrayList(copy: Boolean = false): ArrayList<E> {

    if (this == null) {
        return ArrayList(0)
    }

    return if (!copy && this is java.util.ArrayList<E>) {
        this
    } else ArrayList(this)

}

fun <E> List<E>?.ifNotEmpty(action: List<E>.() -> Unit) {
    if (!this.isNullOrEmpty()) {
        action.invoke(this)
    }
}

fun <E> MutableList<E>.removeWhich(firstMatchOnly: Boolean = false, filter: (E) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
        if (firstMatchOnly && removed) {
            return removed
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