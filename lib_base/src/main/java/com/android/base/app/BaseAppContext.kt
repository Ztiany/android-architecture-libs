package com.android.base.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration

open class BaseAppContext : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Sword.coreAppDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        Sword.coreAppDelegate.onCreate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Sword.coreAppDelegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Sword.coreAppDelegate.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Sword.coreAppDelegate.onConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        Sword.coreAppDelegate.onTerminate()
    }

}