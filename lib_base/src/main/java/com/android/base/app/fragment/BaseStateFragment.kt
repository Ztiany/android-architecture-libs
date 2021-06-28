package com.android.base.app.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.R
import com.android.base.app.ui.RefreshStateLayout
import com.android.base.app.ui.RefreshView
import com.android.base.app.ui.RefreshView.RefreshHandler
import com.android.base.app.ui.StateLayoutConfig
import com.android.base.app.ui.StateLayoutConfig.RetryableState
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise

/**
 * 1. 支持显示{CONTENT, LOADING, ERROR, EMPTY}等状态布局、支持下拉刷新
 * 2. 使用的布局中必须有一个id = [R.id.base_state_layout] 的 Layout，确保 Layout 实现了[com.android.base.app.ui.StateLayout]
 * 3. [RefreshView] (下拉刷新)的 id 必须设置为 ：[R.id.base_refresh_layout]，没有添加则表示不需要下拉刷新功能
 * 4. 默认所有重试和下拉刷新都会调用 [onRefresh]，子类可以修改该行为
 *
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 */
abstract class BaseStateFragment : BaseUIFragment(), RefreshStateLayout {

    private lateinit var stateLayout: RefreshableStateLayoutImpl

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        stateLayout = RefreshableStateLayoutImpl.init(view)
        stateLayout.setRefreshHandler(object : RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateFragment.onRefresh()
            }

            override fun canRefresh(): Boolean {
                return this@BaseStateFragment.canRefresh()
            }
        })
        stateLayout.setStateRetryListenerUnchecked { state -> onRetry(state) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    internal open fun canRefresh() = true

    private val refreshView: RefreshView?
        get() = stateLayout.refreshView

    protected open fun onRetry(@RetryableState state: Int) {
        refreshView.ifNonNull {
            if (!this.isRefreshing) {
                autoRefresh()
            }
        } otherwise {
            onRefresh()
        }
    }

    protected open fun onRefresh() {}

    fun setRefreshEnable(enable: Boolean) = refreshView?.setRefreshEnable(enable)

    override fun getStateLayoutConfig(): StateLayoutConfig = stateLayout.stateLayoutConfig

    override fun isRefreshing() = stateLayout.isRefreshing

    override fun refreshCompleted() = stateLayout.refreshCompleted()

    override fun autoRefresh() = stateLayout.autoRefresh()

    override fun showContentLayout() = stateLayout.showContentLayout()

    override fun showLoadingLayout() = stateLayout.showLoadingLayout()

    override fun showEmptyLayout() = stateLayout.showEmptyLayout()

    override fun showErrorLayout() = stateLayout.showErrorLayout()

    override fun showRequesting() = stateLayout.showRequesting()

    override fun showBlank() = stateLayout.showBlank()

    override fun showNetErrorLayout() = stateLayout.showNetErrorLayout()

    override fun showServerErrorLayout() = stateLayout.showServerErrorLayout()

    override fun currentStatus() = stateLayout.currentStatus()

    companion object {
        const val CONTENT = StateLayoutConfig.CONTENT
        const val LOADING = StateLayoutConfig.LOADING
        const val ERROR = StateLayoutConfig.ERROR
        const val EMPTY = StateLayoutConfig.EMPTY
        const val NET_ERROR = StateLayoutConfig.NET_ERROR
        const val SERVER_ERROR = StateLayoutConfig.SERVER_ERROR
    }

}