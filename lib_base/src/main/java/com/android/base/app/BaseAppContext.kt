package com.android.base.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration

open class BaseAppContext : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Sword.applicationDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        Sword.applicationDelegate.onCreate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Sword.applicationDelegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Sword.applicationDelegate.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Sword.applicationDelegate.onConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        Sword.applicationDelegate.onTerminate()
    }

}