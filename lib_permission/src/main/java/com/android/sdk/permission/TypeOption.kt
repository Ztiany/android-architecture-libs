package com.android.sdk.permission

import android.content.Context
import androidx.fragment.app.Fragment
import com.android.sdk.permission.api.InstallRequest
import com.android.sdk.permission.impl.andpermission.AndInstallRequest
import com.android.sdk.permission.utils.HostWrapper

class TypeOption {

    private var hostWrapper: HostWrapper

    constructor(fragment: Fragment) {
        hostWrapper = HostWrapper.create(fragment)
    }

    constructor(context: Context) {
        hostWrapper = HostWrapper.create(context)
    }

    fun runtime(): RuntimeOption {
        return RuntimeOption(hostWrapper)
    }

    fun install(): InstallRequest {
        return AndInstallRequest(hostWrapper)
    }

}