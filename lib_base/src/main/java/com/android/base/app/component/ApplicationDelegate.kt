package com.android.base.app.component

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import com.android.base.CrashProcessor
import com.android.base.app.utils.CrashHandler
import com.android.base.network.NetStateReceiver
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils.OnAppStatusChangedListener
import io.reactivex.processors.BehaviorProcessor
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-10-12 18:19
 */
class ApplicationDelegate internal constructor() {

    private lateinit var application: Application

    private lateinit var crashHandler: CrashHandler

    /** 获取可观察的 app 生命周期  */
    val appStatus: BehaviorProcessor<Boolean> = BehaviorProcessor.create()

    private val onCreateCalled = AtomicBoolean(false)

    private val onAttachBaseCalled = AtomicBoolean(false)

    fun attachBaseContext(base: Context) {
        check(onAttachBaseCalled.compareAndSet(false, true)) { "Can only be called once" }
        //app lifecycle invoke
        Timber.d("attachBaseContext = ${ServiceLoader.load(AppLifecycle::class.java)}")
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.attachBaseContext(base)
            }
    }

    fun onCreate(application: Application) {
        check(onCreateCalled.compareAndSet(false, true)) { "Can only be called once" }
        this.application = application
        //工具类初始化
        BaseUtils.init(application)
        //异常日志记录
        crashHandler = CrashHandler.register(application)
        //网络状态
        application.registerReceiver(
            NetStateReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        //App前台后台
        listenActivityLifecycleCallbacks()
        //app lifecycle invoke
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.onCreate(application)
            }
    }

    fun onTerminate() {
        //app lifecycle invoke
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.onTerminate()
            }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        //app lifecycle invoke
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.onConfigurationChanged(newConfig)
            }
    }

    fun onTrimMemory(level: Int) {
        //app lifecycle invoke
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.onTrimMemory(level)
            }
    }

    fun onLowMemory() {
        //app lifecycle invoke
        ServiceLoader.load(AppLifecycle::class.java)
            .forEach {
                it.onLowMemory()
            }
    }

    private fun listenActivityLifecycleCallbacks() {
        AppUtils.registerAppStatusChangedListener(object : OnAppStatusChangedListener {
            override fun onBackground(activity: Activity?) {
                Timber.d("app进入后台")
                appStatus.onNext(false)
            }

            override fun onForeground(activity: Activity?) {
                Timber.d("app进入前台")
                appStatus.onNext(true)
            }
        })
    }

    internal fun setCrashProcessor(crashProcessor: CrashProcessor) {
        crashHandler.setCrashProcessor(crashProcessor)
    }

}