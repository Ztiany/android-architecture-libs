package com.android.base.architecture.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.base.AndroidSword

open class BaseAppContext : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AndroidSword.coreAppDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidSword.coreAppDelegate.onCreate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AndroidSword.coreAppDelegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AndroidSword.coreAppDelegate.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AndroidSword.coreAppDelegate.onConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        AndroidSword.coreAppDelegate.onTerminate()
    }

}