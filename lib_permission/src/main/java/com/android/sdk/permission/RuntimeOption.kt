package com.android.sdk.permission

import androidx.fragment.app.FragmentActivity
import com.android.sdk.permission.utils.HostWrapper
import com.android.sdk.permission.andpermission.AndPermissionRequest
import com.android.sdk.permission.andpermission.AndSettingRequest
import com.android.sdk.permission.api.PermissionRequest
import com.android.sdk.permission.api.SettingRequest
import com.android.sdk.permission.internal.InternalPermissionRequester

class RuntimeOption(private val hostWrapper: HostWrapper) {

    fun permission(vararg groups: String): PermissionRequest {
        return if (AutoPermission.isUseInternalApi()) {
            internalPermissionRequester(groups)
        } else {
            AndPermissionRequest(hostWrapper, groups)
        }
    }

    private fun internalPermissionRequester(groups: Array<out String>): InternalPermissionRequester {
        return if (hostWrapper.fragment != null) {
            val fragmentActivity = hostWrapper.context as FragmentActivity
            InternalPermissionRequester(fragmentActivity, hostWrapper.fragment, groups)
        } else {
            val fragmentActivity = hostWrapper.context as FragmentActivity
            InternalPermissionRequester(fragmentActivity, fragmentActivity, groups)
        }
    }

    fun setting(): SettingRequest {
        return AndSettingRequest(hostWrapper)
    }

}