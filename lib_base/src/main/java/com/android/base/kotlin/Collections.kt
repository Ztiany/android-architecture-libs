package com.android.base.kotlin

import com.android.base.utils.common.CollectionUtils

fun <E> List<E>?.toArrayList(): ArrayList<E> {
    return CollectionUtils.toArrayList(this)
}

fun <E> MutableList<E>.removeWhich(filter: (E) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}