package com.android.base.kotlin

import com.android.base.utils.android.compat.AndroidVersion


inline fun ifSDKAbove(sdkVersion: Int, block: () -> Unit) {
    if (AndroidVersion.above(sdkVersion)) {
        block()
    }
}

inline fun ifSDKAt(sdkVersion: Int, block: () -> Unit) {
    if (AndroidVersion.at(sdkVersion)) {
        block()
    }
}

inline fun ifSDKAtLeast(sdkVersion: Int, block: () -> Unit) {
    if (AndroidVersion.atLeast(sdkVersion)) {
        block()
    }
}

inline fun ignoreCrash(code: () -> Unit) {
    try {
        code()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
