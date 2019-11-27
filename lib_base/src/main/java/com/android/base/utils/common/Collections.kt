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