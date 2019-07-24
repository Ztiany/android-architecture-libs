package com.android.base.kotlin

import com.android.base.utils.common.CollectionUtils

fun <T> List<T>?.toArrayList(): ArrayList<T> {
    return CollectionUtils.toArrayList(this)
}