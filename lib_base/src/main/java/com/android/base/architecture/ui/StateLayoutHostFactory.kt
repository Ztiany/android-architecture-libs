package com.android.base.architecture.ui

import com.android.base.architecture.ui.list.RefreshView
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayout
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.architecture.ui.state.StateLayoutHost

class StateLayoutHostConfig {
    var onRetry: ((state: Int) -> Unit)? = null
    var onRefresh: (() -> Unit)? = null
}

/** It is useful when there is more than one state layout in a fragment. */
fun buildStateLayoutHost(
    stateLayout: StateLayout,
    refreshView: RefreshView? = null,
    config: StateLayoutHostConfig.() -> Unit
): StateLayoutHost {

    val hostConfig = StateLayoutHostConfig().apply(config)

    refreshView?.setRefreshHandler(object : RefreshView.RefreshHandler() {
        override fun onRefresh() {
            hostConfig.onRefresh?.invoke()
        }
    })

    stateLayout.stateLayoutConfig.setStateRetryListener(object : OnRetryActionListener {
        override fun onRetry(state: Int) {
            hostConfig.onRetry?.invoke(state)
        }
    })

    return object : StateLayoutHost {

        override fun autoRefresh() {
            refreshView?.autoRefresh()
        }

        override fun refreshCompleted() {
            refreshView?.refreshCompleted()
        }

        override fun isRefreshing(): Boolean {
            return refreshView?.isRefreshing ?: false
        }

        override fun isRefreshEnable(): Boolean {
            return refreshView?.isRefreshEnable ?: false
        }

        override fun showContentLayout() {
            stateLayout.showContentLayout()
        }

        override fun showLoadingLayout() {
            stateLayout.showLoadingLayout()
        }

        override fun showEmptyLayout() {
            stateLayout.showEmptyLayout()
        }

        override fun showErrorLayout() {
            stateLayout.showErrorLayout()
        }

        override fun showRequesting() {
            stateLayout.showRequesting()
        }

        override fun showBlank() {
            stateLayout.showBlank()
        }

        override fun showNetErrorLayout() {
            stateLayout.showNetErrorLayout()
        }

        override fun showServerErrorLayout() {
            stateLayout.showServerErrorLayout()
        }

        override fun getStateLayoutConfig(): StateLayoutConfig {
            return stateLayout.stateLayoutConfig
        }

        override fun currentStatus(): Int {
            return stateLayout.currentStatus()
        }

    }

}