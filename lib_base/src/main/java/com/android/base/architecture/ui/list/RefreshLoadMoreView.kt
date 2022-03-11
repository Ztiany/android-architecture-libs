package com.android.base.architecture.ui.list

/**
 *对下拉刷新的抽象
 *
 * @author Ztiany
 * @version 1.0
 */
interface RefreshLoadMoreView {

    fun autoRefresh()

    fun refreshCompleted()

    fun loadMoreCompleted(hasMore: Boolean)

    fun loadMoreFailed()

    fun setRefreshHandler(refreshHandler: RefreshHandler?)

    fun setLoadMoreHandler(loadMoreHandler: LoadMoreHandler?)

    fun isRefreshing(): Boolean

    fun isLoadingMore(): Boolean

    var isRefreshEnable: Boolean

    var isLoadMoreEnable: Boolean

    interface RefreshHandler {
        fun onRefresh()
    }

    interface LoadMoreHandler {
        fun onLoadMore()
    }

}