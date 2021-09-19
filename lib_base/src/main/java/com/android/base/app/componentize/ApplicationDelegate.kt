package com.android.base.app.componentize

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import com.android.base.app.CrashProcessor
import com.android.base.app.utils.CrashHandler
import com.android.base.receiver.NetStateReceiver
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils.OnAppStatusChangedListener
import io.reactivex.processors.BehaviorProcessor
import timber.log.Timber
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
    }

    fun onTerminate() {}

    fun onConfigurationChanged(newConfig: Configuration) {}

    fun onTrimMemory(level: Int) {}

    fun onLowMemory() {}

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