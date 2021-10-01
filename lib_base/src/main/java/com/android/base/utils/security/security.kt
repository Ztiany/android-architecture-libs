package com.android.base.utils.security

fun md5(content: String): String {
    return MD5Utils.md5(content) ?: ""
}