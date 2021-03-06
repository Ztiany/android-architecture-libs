package com.android.sdk.permission.api

import com.android.sdk.permission.impl.selfstudy.IPermissionUIProvider

interface PermissionRequest {

    fun onGranted(granted: (List<String>) -> Unit): PermissionRequest

    fun onDenied(denied: (List<String>) -> Unit): PermissionRequest

    fun showTips(showTips: Boolean): PermissionRequest

    fun askAgain(askAgain: Boolean): PermissionRequest

    fun customUI(uiProvider: IPermissionUIProvider): PermissionRequest

    fun start()

}