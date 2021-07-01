package com.android.base.componentization

import android.app.Application
import android.content.Context

/**
 * For usage, refer to [ComponentizationAppContext].
 */
object ComponentizationSDK : AppLifecycle {

    internal lateinit var globalConfigHolder: GlobalConfigHolder

    private lateinit var application: Application

    private val components = mutableListOf<AppLifecycle>()

    fun init(app: Application) {
        application = app
        globalConfigHolder = parseAndBuild()
    }

    private fun parseAndBuild(): GlobalConfigHolder {
        val componentConfigs = ManifestParser(application).parse()

        val holderBuilder = GlobalConfigHolder.Builder()

        componentConfigs.forEach {
            it.configApp(holderBuilder)
            it.registerAppLifecycle(object : AppLifecycleRegister {
                override fun register(appLifecycle: AppLifecycle) {
                    components.add(appLifecycle)
                }
            })
        }

        return holderBuilder.build()
    }

    override fun attachBaseContext(base: Context) {
        components.forEach {
            it.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        components.forEach {
            it.onCreate(application)
        }
    }

    override fun onTerminate() {
        components.forEach {
            it.onTerminate()
        }
    }

}