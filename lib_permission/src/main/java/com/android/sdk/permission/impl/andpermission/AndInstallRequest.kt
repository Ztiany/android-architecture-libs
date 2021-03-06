package com.android.sdk.permission.impl.andpermission

import com.android.sdk.permission.utils.HostWrapper
import com.android.sdk.permission.api.InstallRequest
import com.android.sdk.permission.utils.toAndPermission
import java.io.File

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-22 18:55
 */

class AndInstallRequest(hostWrapper: HostWrapper) : InstallRequest {

    private val installRequest = hostWrapper.toAndPermission().install()

    override fun file(file: File): InstallRequest {
        installRequest.file(file)
        return this
    }

    override fun onGranted(granted: (File) -> Unit): InstallRequest {
        installRequest.onGranted {
            granted(it)
        }
        return this
    }

    override fun onDenied(denied: (File) -> Unit): InstallRequest {
        installRequest.onDenied {
            denied(it)
        }
        return this
    }

    override fun start() {
        installRequest.start()
    }

}