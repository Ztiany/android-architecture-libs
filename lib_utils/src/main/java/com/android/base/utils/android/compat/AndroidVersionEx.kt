package com.android.base.utils.android.compat


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

fun isSDKAbove(sdkVersion: Int) = AndroidVersion.above(sdkVersion)

fun isSDKAt(sdkVersion: Int) = AndroidVersion.at(sdkVersion)

fun isSDKAtLeast(sdkVersion: Int) = AndroidVersion.atLeast(sdkVersion)
