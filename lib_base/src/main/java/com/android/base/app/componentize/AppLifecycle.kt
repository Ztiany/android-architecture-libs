package com.android.base.app.componentize

import android.app.Application
import android.content.Context

interface AppLifecycle {

    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate()

}