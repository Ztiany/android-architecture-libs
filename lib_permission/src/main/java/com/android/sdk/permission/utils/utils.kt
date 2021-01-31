package com.android.sdk.permission.utils

import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.option.Option

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-22 17:13
 */
fun HostWrapper.toAndPermission(): Option {
    return if (fragment != null) {
        AndPermission.with(fragment)
    } else {
        AndPermission.with(context)
    }
}