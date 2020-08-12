package com.android.base.app

import android.app.Activity
import android.content.Context
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.app.fragment.animator.FragmentAnimator
import com.android.base.foundation.fragment.FragmentDelegateOwner
import com.android.base.app.fragment.tools.FragmentConfig
import com.android.base.app.ui.LoadingView
import com.android.base.app.ui.PageNumber
import com.android.base.app.ui.RefreshLoadViewFactory
import com.android.base.app.ui.RefreshLoadViewFactory.Factory
import com.android.base.app.ui.RefreshViewFactory
import com.android.base.receiver.NetworkState
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import io.reactivex.Flowable
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

/**
 * useful tools for android development, just like a sword.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:12
 */
object Sword {

    private val androidComponentLifecycleInjector = AndroidComponentLifecycleInjector()

    /** Application lifecycle delegate */
    internal val applicationDelegate = ApplicationDelegate(androidComponentLifecycleInjector)

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

    /** RxJava2的一个重要的设计理念是：不吃掉任何一个异常。产生的问题是，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，这时由于已经取消订阅，
     * “downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃,解决方案：在Application设置RxJavaPlugin的ErrorHandler。
     * refer: [RxJava2使用过程中遇到的坑](https://github.com/qqiabc521/blog/issues/3) */
    fun setupRxJavaErrorHandler(): Sword {
        RxJavaPlugins.setErrorHandler {
            Timber.d("RxJavaPlugins error handler receives error: $it")
        }
        return this
    }

    fun setDelegateInjector(delegateInjector: DelegateInjector): Sword {
        androidComponentLifecycleInjector.delegateInjector = delegateInjector
        return this
    }

    /** 获取可观察的 app 生命周期，发射 tue 表示 app 切换到前台，发射 false 表示 app 切换到后台  */
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

interface DelegateInjector {

    fun injectFragmentDelegate(fragment: FragmentDelegateOwner)

    fun injectActivityDelegate(activity: ActivityDelegateOwner)

}
