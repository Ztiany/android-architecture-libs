@file:JvmName("Threads")

package com.android.base.utils.android

import android.os.Looper

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}