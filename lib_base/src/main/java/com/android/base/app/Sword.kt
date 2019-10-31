package com.android.base.app

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.animator.FragmentAnimator
import com.android.base.app.fragment.tools.FragmentConfig
import com.android.base.app.ui.LoadingView
import com.android.base.app.ui.PageNumber
import com.android.base.app.ui.RefreshLoadViewFactory
import com.android.base.app.ui.RefreshLoadViewFactory.Factory
import com.android.base.app.ui.RefreshViewFactory
import com.android.base.interfaces.ActivityLifecycleCallbacksAdapter
import com.android.base.receiver.NetworkState
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Flowable

/**
 * useful tools for android development, just like a sword.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:12
 */
object Sword {

    /** Application lifecycle delegate */
    val applicationDelegate = ApplicationDelegate()

    /** 错误类型分类器 */
    var errorClassifier: ErrorClassifier? = null

    /** dialog 最小展示时间  */
    var minimumShowingDialogMills: Long = 0

    /** 用于创建 LoadingView*/
    var loadingViewFactory: ((Context) -> LoadingView)? = null

    /**网络状态监听器*/
    fun networkState(): Flowable<NetworkState> = NetworkState.observableState()

    fun setCrashProcessor(crashProcessor: CrashProcessor): Sword {
        applicationDelegate.setCrashProcessor(crashProcessor)
        return this
    }

    fun setDefaultPageStart(pageStart: Int): Sword {
        PageNumber.setDefaultPageStart(pageStart)
        return this
    }

    fun setDefaultPageSize(defaultPageSize: Int): Sword {
        PageNumber.setDefaultPageSize(defaultPageSize)
        return this
    }

    /** 设置一个默认的布局 id，在使用 Fragments 中相关方法时，如果没有传入特定的容器 id  时，则使用设置的默认布局 id。  */
    fun setDefaultFragmentContainerId(defaultContainerId: Int): Sword {
        FragmentConfig.setDefaultContainerId(defaultContainerId)
        return this
    }

    /**设置默认的 Fragment 转场动画*/
    fun setDefaultFragmentAnimator(animator: FragmentAnimator?): Sword {
        FragmentConfig.setDefaultFragmentAnimator(animator)
        return this
    }

    fun enableAutoInject(): Sword {
        val activityLifecycleCallbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacksAdapter {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity is Injectable) {
                    if ((activity as Injectable).enableInject()) {
                        AndroidInjection.inject(activity)
                        if (activity is FragmentActivity) {
                            handedFragmentInject(activity as FragmentActivity)
                        }
                    }
                }
            }

            private fun handedFragmentInject(activity: FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks() {
                    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                        if (f is Injectable) {
                            if ((f as Injectable).enableInject()) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }
                }, true)
            }
        }
        applicationDelegate.application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        return this
    }

    /** 获取可观察的 app 生命周期  */
    val appState: Flowable<Boolean>
        get() = applicationDelegate.appStatus

    /** 获取当前 resume 的 Activity */
    val topActivity: Activity?
        get() = ActivityUtils.getTopActivity()

    /** App是否在前台运行 */
    val isForeground: Boolean
        get() = AppUtils.isAppForeground()

    fun registerRefreshLoadViewFactory(factory: Factory): Sword {
        RefreshLoadViewFactory.registerFactory(factory)
        return this
    }

    fun registerRefreshViewFactory(factory: RefreshViewFactory.Factory): Sword {
        RefreshViewFactory.registerFactory(factory)
        return this
    }

}

interface CrashProcessor {
    fun uncaughtException(thread: Thread, ex: Throwable)
}

interface ErrorClassifier {
    fun isNetworkError(throwable: Throwable): Boolean
    fun isServerError(throwable: Throwable): Boolean
}