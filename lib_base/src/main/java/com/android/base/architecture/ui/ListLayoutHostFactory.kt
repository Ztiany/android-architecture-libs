package com.android.base.architecture.ui

import androidx.recyclerview.widget.RecyclerView.Adapter
import com.android.base.architecture.ui.list.*
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayout
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.foundation.adapter.DataManager
import com.ztiany.loadmore.adapter.OnLoadMoreListener
import com.ztiany.loadmore.adapter.WrapperAdapter

class ListLayoutHostConfig {
    var enableLoadMore: Boolean = false
    var onRetry: ((state: Int) -> Unit)? = null
    var onRefresh: (() -> Unit)? = null
    var onLoadMore: (() -> Unit)? = null
}

/** It is useful when there is more than one list layout in a fragment. */
fun <T, L> buildListLayoutHost(
    dataManager: L,
    stateLayout: StateLayout,
    refreshView: RefreshView,
    config: ListLayoutHostConfig.() -> Unit
): ListLayoutHost<T> where L : DataManager<T>, L : Adapter<*> {

    val hostConfig = ListLayoutHostConfig().apply(config)

    val loadMore = if (hostConfig.enableLoadMore) {
        WrapperAdapter.wrap(dataManager)
    } else {
        null
    }

    refreshView.setRefreshHandler(object : RefreshView.RefreshHandler() {
        override fun onRefresh() {
            hostConfig.onRefresh?.invoke()
        }

        override fun canRefresh(): Boolean {
            return if (loadMore != null) {
                !loadMore.isLoadingMore
            } else true
        }
    })

    stateLayout.stateLayoutConfig.setStateRetryListener(object : OnRetryActionListener {
        override fun onRetry(state: Int) {
            hostConfig.onRetry?.invoke(state)
        }
    })

    loadMore?.setOnLoadMoreListener(object : OnLoadMoreListener {
        override fun onLoadMore() {
            hostConfig.onLoadMore?.invoke()
        }

        override fun canLoadMore() = !refreshView.isRefreshing
    })

    return object : ListLayoutHost<T> {

        override fun replaceData(data: List<T>) {
            dataManager.replaceAll(data)
        }

        override fun addData(data: List<T>) {
            dataManager.addItems(data)
        }

        override fun loadMoreCompleted(hasMore: Boolean) {
            loadMore?.loadCompleted(hasMore)
        }

        override fun loadMoreFailed() {
            loadMore?.loadFail()
        }

        override fun getPager(): Paging {
            return AutoPaging(this, dataManager)
        }

        override fun isEmpty(): Boolean {
            return dataManager.isEmpty()
        }

        override fun isLoadingMore(): Boolean {
            return loadMore?.isLoadingMore ?: false
        }

        override fun isLoadMoreEnable(): Boolean {
            return hostConfig.enableLoadMore
        }

        override fun autoRefresh() {
            refreshView.autoRefresh()
        }

        override fun refreshCompleted() {
            refreshView.refreshCompleted()
        }

        override fun isRefreshing(): Boolean {
            return refreshView.isRefreshing
        }

        override fun isRefreshEnable(): Boolean {
            return refreshView.isRefreshEnable
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

/** It is useful when there is more than one list layout in a fragment. */
fun <T> buildListLayoutHost(
    dataManager: DataManager<T>,
    stateLayout: StateLayout,
    refreshLoadMoreView: RefreshLoadMoreView,
    config: ListLayoutHostConfig.() -> Unit
): ListLayoutHost<T> {

    val hostConfig = ListLayoutHostConfig().apply(config)

    refreshLoadMoreView.setRefreshHandler(object : RefreshLoadMoreView.RefreshHandler {
        override fun onRefresh() {
            hostConfig.onRefresh?.invoke()
        }
    })

    refreshLoadMoreView.setLoadMoreHandler(object : RefreshLoadMoreView.LoadMoreHandler {
        override fun onLoadMore() {
            hostConfig.onLoadMore?.invoke()
        }
    })

    stateLayout.stateLayoutConfig.setStateRetryListener(object : OnRetryActionListener {
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
            refreshLoadMoreView.loadMoreCompleted(hasMore)
        }

        override fun loadMoreFailed() {
            refreshLoadMoreView?.loadMoreFailed()
        }

        override fun getPager(): Paging {
            return AutoPaging(this, dataManager)
        }

        override fun isEmpty(): Boolean {
            return dataManager.isEmpty()
        }

        override fun isLoadingMore(): Boolean {
            return refreshLoadMoreView.isLoadingMore()
        }

        override fun isLoadMoreEnable(): Boolean {
            return hostConfig.enableLoadMore
        }

        override fun autoRefresh() {
            refreshLoadMoreView.autoRefresh()
        }

        override fun refreshCompleted() {
            refreshLoadMoreView.refreshCompleted()
        }

        override fun isRefreshing(): Boolean {
            return refreshLoadMoreView.isRefreshing()
        }

        override fun isRefreshEnable(): Boolean {
            return refreshLoadMoreView.isRefreshEnable
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