package com.android.base.componentization

interface ComponentConfig {

    fun configApp(builder: GlobalConfigHolder.Builder)

    fun registerAppLifecycle(appLifecycleRegister: AppLifecycleRegister)

}