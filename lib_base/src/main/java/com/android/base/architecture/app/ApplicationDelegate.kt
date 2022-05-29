package com.android.base.architecture.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.base.CrashProcessor
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.AppUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ztiany
 */
class ApplicationDelegate internal constructor() {

    private lateinit var application: Application

    private lateinit var crashHandler: CrashHandler

    /** 获取可观察的 app 生命周期  */
    private val _appStatus = MutableStateFlow(true)

    val appStatus: Flow<Boolean> = _appStatus

    private val onCreateCalled = AtomicBoolean(false)

    private val onAttachBaseCalled = AtomicBoolean(false)

    fun attachBaseContext(@Suppress("UNUSED_PARAMETER") base: Context) {
        check(onAttachBaseCalled.compareAndSet(false, true)) { "Can only be called once" }
    }

    fun onCreate(application: Application) {
        check(onCreateCalled.compareAndSet(false, true)) { "Can only be called once" }
        this.application = application
        //工具类初始化
        BaseUtils.init(application)
        //异常日志记录
        crashHandler = CrashHandler.register(application)
        //App前台后台
        listenActivityLifecycleCallbacks()
    }

    fun onTerminate() {
    }

    fun onConfigurationChanged(@Suppress("UNUSED_PARAMETER") newConfig: Configuration) {
    }

    fun onTrimMemory(@Suppress("UNUSED_PARAMETER") level: Int) {
    }

    fun onLowMemory() {
    }

    private fun listenActivityLifecycleCallbacks() {
        AppUtils.addOnAppStatusChangedListener(object : AppUtils.OnAppStatusChangedListener {
            override fun onBackground(activity: Activity?) {
                Timber.d("app进入后台")
                _appStatus.value = false
            }

            override fun onForeground(activity: Activity?) {
                Timber.d("app进入前台")
                _appStatus.value = true
            }
        })
    }

    internal fun setCrashProcessor(crashProcessor: CrashProcessor) {
        crashHandler.setCrashProcessor(crashProcessor)
    }

}