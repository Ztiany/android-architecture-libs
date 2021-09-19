package com.android.base.app.component

import android.app.Application
import android.content.Context
import android.content.res.Configuration

interface AppLifecycle {

    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate()

    fun onTrimMemory(level: Int)

    fun onLowMemory()

    fun onConfigurationChanged(newConfig: Configuration)

}