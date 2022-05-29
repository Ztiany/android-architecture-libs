package com.android.base.architecture.app

import android.app.Application
import android.content.res.Configuration

/**
 * This interface is designed for initialize business modules which consist of a completed APP. How to collect Module Initializer?
 *
 * option1: use Dagger2.
 * ```
 * class AppContext: Application{
 *
 *  @Inject internal lateinit var moduleInitializers: Lazy<Set<AppLifecycle>>
 *
 * }
 * ```
 *
 * option2: use ServiceLoader.
 *
 * Here it just provide a interface and nothing else.
 */
interface AppLifecycle {

    fun onCreate(application: Application)

    fun onTerminate()

    fun onTrimMemory(level: Int)

    fun onLowMemory()

    fun onConfigurationChanged(newConfig: Configuration)

}