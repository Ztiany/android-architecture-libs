package com.android.base

import android.app.Activity
import android.content.Context
import com.android.base.architecture.app.ApplicationDelegate
import com.android.base.architecture.fragment.animator.FragmentAnimator
import com.android.base.architecture.fragment.tools.FragmentConfig
import com.android.base.architecture.ui.loading.LoadingViewHost
import com.android.base.architecture.ui.list.Paging
import com.android.base.architecture.ui.list.RefreshLoadMoreViewFactory
import com.android.base.architecture.ui.list.RefreshLoadMoreViewFactory.Factory
import com.android.base.architecture.ui.list.RefreshViewFactory
import com.android.base.utils.android.AppUtils
import com.android.base.utils.android.network.NetworkState
import com.android.base.utils.android.network.NetworkUtils
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
    var sLoadingViewHostFactory: ((Context) -> LoadingViewHost)? = null

    /** [Throwable] 到可读的 [CharSequence] 转换*/
    var errorConvert: ErrorConvert = object : ErrorConvert {
        override fun convert(throwable: Throwable): CharSequence {
            return throwable.message.toString()
        }
    }

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
        get() = AppUtils.getTopActivity()

    /** App是否在前台运行 */
    val isForeground: Boolean
        get() = AppUtils.isAppForeground()

    fun registerRefreshLoadViewFactory(factory: Factory): AndroidSword {
        RefreshLoadMoreViewFactory.registerFactory(factory)
        return this
    }

    fun registerRefreshViewFactory(factory: RefreshViewFactory.Factory): AndroidSword {
        RefreshViewFactory.registerFactory(factory)
        return this
    }

    /** 监听网络状态 */
    fun observableNetworkState(): Flow<NetworkState> = NetworkUtils.observableNetworkState()

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
