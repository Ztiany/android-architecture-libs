package com.android.base

import android.app.Activity
import android.content.Context
import com.android.base.architecture.app.ApplicationDelegate
import com.android.base.architecture.fragment.animator.FragmentAnimator
import com.android.base.architecture.fragment.tools.FragmentConfig
import com.android.base.architecture.ui.LoadingView
import com.android.base.architecture.ui.Paging
import com.android.base.architecture.ui.RefreshLoadViewFactory
import com.android.base.architecture.ui.RefreshLoadViewFactory.Factory
import com.android.base.architecture.ui.RefreshViewFactory
import com.android.base.utils.android.network.NetworkState
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import kotlinx.coroutines.flow.Flow

/**
 * A set of useful tools for android development, just like a sword.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:12
 */
object AndroidSword {

    /** Application lifecycle delegate */
    val coreAppDelegate = ApplicationDelegate()

    /** 错误类型分类器 */
    var errorClassifier: ErrorClassifier? = null

    /** dialog 最小展示时间  */
    var minimumShowingDialogMills: Long = 0

    /** 用于创建 LoadingView*/
    var loadingViewFactory: ((Context) -> LoadingView)? = null

    /** [Throwable] 到可读的 [CharSequence] 转换*/
    var errorConvert: ErrorConvert = object : ErrorConvert {
        override fun convert(throwable: Throwable): CharSequence {
            return throwable.message.toString()
        }
    }

    /**网络状态监听器*/
    fun networkState(): Flow<NetworkState> = coreAppDelegate.observableNetworkState()

    fun setCrashProcessor(crashProcessor: CrashProcessor): AndroidSword {
        coreAppDelegate.setCrashProcessor(crashProcessor)
        return this
    }

    fun setDefaultPageStart(pageStart: Int): AndroidSword {
        Paging.setDefaultPageStart(pageStart)
        return this
    }

    fun setDefaultPageSize(defaultPageSize: Int): AndroidSword {
        Paging.setDefaultPageSize(defaultPageSize)
        return this
    }

    /** 设置一个默认的布局 id，在使用 Fragments 中相关方法时，如果没有传入特定的容器 id  时，则使用设置的默认布局 id。  */
    fun setDefaultFragmentContainerId(defaultContainerId: Int): AndroidSword {
        FragmentConfig.setDefaultContainerId(defaultContainerId)
        return this
    }

    /**设置默认的 Fragment 转场动画*/
    fun setDefaultFragmentAnimator(animator: FragmentAnimator?): AndroidSword {
        FragmentConfig.setDefaultFragmentAnimator(animator)
        return this
    }

    /** 获取可观察的 app 生命周期，发射 true 表示 app 切换到前台，发射 false 表示 app 切换到后台  */
    val appState: Flow<Boolean>
        get() = coreAppDelegate.appStatus

    /** 获取当前 resume 的 Activity */
    val topActivity: Activity?
        get() = ActivityUtils.getTopActivity()

    /** App是否在前台运行 */
    val isForeground: Boolean
        get() = AppUtils.isAppForeground()

    fun registerRefreshLoadViewFactory(factory: Factory): AndroidSword {
        RefreshLoadViewFactory.registerFactory(factory)
        return this
    }

    fun registerRefreshViewFactory(factory: RefreshViewFactory.Factory): AndroidSword {
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

interface ErrorConvert {
    fun convert(throwable: Throwable): CharSequence
}
