package com.android.sdk.permission.impl.andpermission

import com.android.sdk.permission.utils.HostWrapper
import com.android.sdk.permission.api.SettingRequest
import com.android.sdk.permission.utils.toAndPermission

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-22 18:54
 */
class AndSettingRequest(private val hostWrapper: HostWrapper) : SettingRequest {

    override fun start(requestCode: Int) {
        hostWrapper.toAndPermission().runtime().setting().start(requestCode)
    }

}