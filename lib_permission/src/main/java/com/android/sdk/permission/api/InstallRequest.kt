package com.android.sdk.permission.api

import java.io.File

interface InstallRequest {

    fun file(file: File): InstallRequest

    fun onGranted(granted: (File) -> Unit): InstallRequest

    fun onDenied(denied: (File) -> Unit): InstallRequest

    fun start()

}