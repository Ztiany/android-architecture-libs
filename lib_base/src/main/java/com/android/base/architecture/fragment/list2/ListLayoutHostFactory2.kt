package com.android.base.architecture.fragment.list2

import android.view.View
import com.android.base.architecture.ui.list.*
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayout
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.foundation.adapter.DataManager

class ListLayoutHostConfig2 {
    var onRetry: ((state: Int) -> Unit)? = null
    var onRefresh: (() -> Unit)? = null
    var onLoadMore: (() -> Unit)? = null
}

/** It is useful when there is more than one list layout in a fragment. */
fun <T> buildListLayoutHost2(
    dataManager: DataManager<T>,
    stateLayout: View,
    refreshLoadMoreView: View,
    config: ListLayoutHostConfig2.() -> Unit
): ListLayoutHost<T> {

    val stateLayoutImpl = (stateLayout as? StateLayout) ?: throw IllegalStateException("Make sure that stateLayout implements StateLayout.")
    val refreshLoadMoreViewImpl = RefreshLoadMoreViewFactory.createRefreshLoadMoreView(refreshLoadMoreView)

    val hostConfig = ListLayoutHostConfig2().apply(config)

    refreshLoadMoreViewImpl.setRefreshHandler(object : RefreshLoadMoreView.RefreshHandler {
        override fun onRefresh() {
            hostConfig.onRefresh?.invoke()
        }
    })

    refreshLoadMoreViewImpl.setLoadMoreHandler(object : RefreshLoadMoreView.LoadMoreHandler {
        override fun onLoadMore() {
            hostConfig.onLoadMore?.invoke()
        }
    })

    stateLayoutImpl.stateLayoutConfig.setStateRetryListener(object : OnRetryActionListener {
        override fun onRetry(state: Int) {
            hostConfig.onRetry?.invoke(state)
        }
    })

    return object : ListLayoutHost<T> {

        override fun replaceData(data: List<T>) {
            dataManager.replaceAll(data)
        }

        override fun addData(data: List<T>) {
            dataManager.addItems(data)
        }

        override fun loadMoreCompleted(hasMore: Boolean) {
            refreshLoadMoreViewImpl.loadMoreCompleted(hasMore)
        }

        override fun loadMoreFailed() {
            refreshLoadMoreViewImpl.loadMoreFailed()
        }

        override fun getPager(): Paging {
            return AutoPaging(this, dataManager)
        }

        override fun isEmpty(): Boolean {
            return dataManager.isEmpty()
        }

        override fun isLoadingMore(): Boolean {
            return refreshLoadMoreViewImpl.isLoadingMore()
        }

        override var isLoadMoreEnable: Boolean
            get() = refreshLoadMoreViewImpl.isLoadMoreEnable
            set(value) {
                refreshLoadMoreViewImpl.isLoadMoreEnable = value
            }

        override fun autoRefresh() {
            refreshLoadMoreViewImpl.autoRefresh()
        }

        override fun refreshCompleted() {
            refreshLoadMoreViewImpl.refreshCompleted()
        }

        override fun isRefreshing(): Boolean {
            return refreshLoadMoreViewImpl.isRefreshing()
        }

        override var isRefreshEnable: Boolean
            get() = refreshLoadMoreViewImpl.isRefreshEnable
            set(value) {
                refreshLoadMoreViewImpl.isRefreshEnable = value
            }

        override fun showContentLayout() {
            stateLayoutImpl.showContentLayout()
        }

        override fun showLoadingLayout() {
            stateLayoutImpl.showLoadingLayout()
        }

        override fun showEmptyLayout() {
            stateLayoutImpl.showEmptyLayout()
        }

        override fun showErrorLayout() {
            stateLayoutImpl.showErrorLayout()
        }

        override fun showRequesting() {
            stateLayoutImpl.showRequesting()
        }

        override fun showBlank() {
            stateLayoutImpl.showBlank()
        }

        override fun showNetErrorLayout() {
            stateLayoutImpl.showNetErrorLayout()
        }

        override fun showServerErrorLayout() {
            stateLayoutImpl.showServerErrorLayout()
        }

        override fun getStateLayoutConfig(): StateLayoutConfig {
            return stateLayoutImpl.stateLayoutConfig
        }

        override fun currentStatus(): Int {
            return stateLayoutImpl.currentStatus()
        }

    }

}