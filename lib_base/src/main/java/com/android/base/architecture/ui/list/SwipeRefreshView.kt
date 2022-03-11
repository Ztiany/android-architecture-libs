package com.android.base.architecture.ui.list

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

internal class SwipeRefreshView(
    private val swipeRefreshLayout: SwipeRefreshLayout
) : RefreshView {

    private var refreshHandler: RefreshView.RefreshHandler? = null

    override fun autoRefresh() {
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            doRefresh()
        }
    }

    override fun refreshCompleted() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun setRefreshHandler(refreshHandler: RefreshView.RefreshHandler?) {
        this.refreshHandler = refreshHandler
        swipeRefreshLayout.setOnRefreshListener { doRefresh() }
    }

    private fun doRefresh() {
        if (refreshHandler?.canRefresh() == true) {
            refreshHandler?.onRefresh()
        } else {
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
        }
    }

    override val isRefreshing: Boolean
        get() = swipeRefreshLayout.isRefreshing

    override var isRefreshEnable: Boolean
        get() = swipeRefreshLayout.isEnabled
        set(enable) {
            swipeRefreshLayout.isEnabled = enable
        }

}