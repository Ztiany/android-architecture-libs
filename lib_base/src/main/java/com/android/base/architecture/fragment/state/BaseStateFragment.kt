package com.android.base.architecture.fragment.state

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.R
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.list.RefreshView
import com.android.base.architecture.ui.list.RefreshView.RefreshHandler
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.architecture.ui.state.StateLayoutConfig.RetryableState
import com.android.base.architecture.ui.state.StateLayoutHost

/**
 * 1. 支持显示{CONTENT, LOADING, ERROR, EMPTY}等状态布局、支持下拉刷新。
 * 2. 使用的布局中必须有一个id = [R.id.base_state_layout] 的 Layout，确保 Layout 实现了[com.android.base.architecture.ui.StateLayout]。
 * 3. [RefreshView] (下拉刷新)的 id 必须设置为 ：[R.id.base_refresh_layout]，没有添加则表示不需要下拉刷新功能。
 * 4. 默认所有重试和下拉刷新都会调用 [onRefresh]，子类可以修改该行为。
 *
 * @author Ztiany
 * Date :   2016-03-19 23:09
 */
abstract class BaseStateFragment<VB : ViewBinding> : BaseUIFragment<VB>(), StateLayoutHost {

    private lateinit var mStateLayout: StateLayoutHostImpl

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        mStateLayout = StateLayoutHostImpl(view)

        mStateLayout.setRefreshHandler(object : RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateFragment.onRefresh()
            }

            override fun canRefresh(): Boolean {
                return this@BaseStateFragment.canRefresh()
            }
        })

        mStateLayout.setStateRetryListenerUnchecked(object : OnRetryActionListener {
            override fun onRetry(state: Int) {
                this@BaseStateFragment.onRetry(state)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    internal open fun canRefresh() = true

    protected open fun onRetry(@RetryableState state: Int) {
        if (mStateLayout.isRefreshEnable()) {
            if (!isRefreshing()) {
                mStateLayout.autoRefresh()
            }
        } else {
            onRefresh()
        }
    }

    protected open fun onRefresh() {}

    fun setRefreshEnable(enable: Boolean) {
        mStateLayout.setRefreshEnable(enable)
    }

    override fun isRefreshEnable(): Boolean {
        return mStateLayout.isRefreshEnable()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig = mStateLayout.stateLayoutConfig

    override fun isRefreshing() = mStateLayout.isRefreshing()

    override fun refreshCompleted() = mStateLayout.refreshCompleted()

    override fun autoRefresh() = mStateLayout.autoRefresh()

    override fun showContentLayout() = mStateLayout.showContentLayout()

    override fun showLoadingLayout() = mStateLayout.showLoadingLayout()

    override fun showEmptyLayout() = mStateLayout.showEmptyLayout()

    override fun showErrorLayout() = mStateLayout.showErrorLayout()

    override fun showRequesting() = mStateLayout.showRequesting()

    override fun showBlank() = mStateLayout.showBlank()

    override fun showNetErrorLayout() = mStateLayout.showNetErrorLayout()

    override fun showServerErrorLayout() = mStateLayout.showServerErrorLayout()

    override fun currentStatus() = mStateLayout.currentStatus()

    @Suppress("UNUSED")
    companion object {
        const val CONTENT = StateLayoutConfig.CONTENT
        const val LOADING = StateLayoutConfig.LOADING
        const val ERROR = StateLayoutConfig.ERROR
        const val EMPTY = StateLayoutConfig.EMPTY
        const val NET_ERROR = StateLayoutConfig.NET_ERROR
        const val SERVER_ERROR = StateLayoutConfig.SERVER_ERROR
    }

}