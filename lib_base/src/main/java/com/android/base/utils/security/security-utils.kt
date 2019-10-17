package com.android.base.utils.security

import android.annotation.SuppressLint

fun md5(content: String): String {
    return MD5Utils.md5(content)
}

@SuppressLint("DefaultLocale")
fun md5UpperCase(content: String): String {
    return MD5Utils.md5(content).toUpperCase()
}
