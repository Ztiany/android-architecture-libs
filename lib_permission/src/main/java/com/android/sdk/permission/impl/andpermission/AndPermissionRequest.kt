package com.android.sdk.permission.impl.andpermission

import com.android.sdk.permission.utils.HostWrapper
import com.android.sdk.permission.api.PermissionRequest
import com.android.sdk.permission.utils.toAndPermission
import com.android.sdk.permission.impl.selfstudy.IPermissionUIProvider

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-22 17:21
 */
class AndPermissionRequest(
        hostWrapper: HostWrapper,
        groups: Array<out String>
) : PermissionRequest {

    private val request = hostWrapper.toAndPermission().runtime().permission(groups)

    override fun onGranted(granted: (List<String>) -> Unit): PermissionRequest {
        request.onDenied {
            granted(it)
        }
        return this
    }

    override fun onDenied(denied: (List<String>) -> Unit): PermissionRequest {
        request.onDenied {
            denied(it)
        }
        return this
    }

    override fun showTips(showTips: Boolean): PermissionRequest {
        // TODO: 2020/10/9  
        return this
    }

    override fun askAgain(askAgain: Boolean): PermissionRequest {
        // TODO: 2020/10/9  
        return this
    }

    override fun customUI(uiProvider: IPermissionUIProvider): PermissionRequest {
        // TODO: 2020/10/9
        return this
    }

    override fun start() {
        request.start()
    }

}