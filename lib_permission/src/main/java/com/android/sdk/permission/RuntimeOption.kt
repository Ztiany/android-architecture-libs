package com.android.sdk.permission

import androidx.fragment.app.FragmentActivity
import com.android.sdk.permission.api.PermissionRequest
import com.android.sdk.permission.api.SettingRequest
import com.android.sdk.permission.impl.andpermission.AndPermissionRequest
import com.android.sdk.permission.impl.andpermission.AndSettingRequest
import com.android.sdk.permission.impl.selfstudy.InternalPermissionRequester
import com.android.sdk.permission.utils.HostWrapper

class RuntimeOption(private val hostWrapper: HostWrapper) {

    fun permission(vararg groups: String): PermissionRequest {
        return if (AutoPermission.isUseInternalApi()) {
            selfPermissionRequester(groups)
        } else {
            AndPermissionRequest(hostWrapper, groups)
        }
    }

    private fun selfPermissionRequester(groups: Array<out String>): InternalPermissionRequester {
        return if (hostWrapper.fragment != null) {
            val fragmentActivity = hostWrapper.context as FragmentActivity
            InternalPermissionRequester(fragmentActivity, hostWrapper.fragment, groups)
        } else {
            val fragmentActivity = hostWrapper.context as FragmentActivity
            InternalPermissionRequester(fragmentActivity, fragmentActivity, groups)
        }
    }

    fun setting(): SettingRequest {
        return if (AutoPermission.isUseInternalApi()) {
            throw UnsupportedOperationException("Unsupported Operation")
        } else {
            AndSettingRequest(hostWrapper)
        }
    }

}