package com.android.base.componentization

import android.content.Context
import android.content.res.Configuration
import com.android.base.app.BaseAppContext

class ComponentizationAppContext : BaseAppContext() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ComponentizationSDK.init(this)
        ComponentizationSDK.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        ComponentizationSDK.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ComponentizationSDK.onTerminate()
    }

}