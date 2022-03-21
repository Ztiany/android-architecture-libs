package com.android.base.architecture.fragment.list

import android.view.View
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
    var triggerLoadMoreByScroll: Boolean = false
    var onRetry: ((state: Int) -> Unit)? = null
    var onLoadMoreCreated: ((WrapperAdapter) -> Unit)? = null
    var onRefresh: (() -> Unit)? = null
    var onLoadMore: (() -> Unit)? = null
}

/** It is useful when there is more than one list layout in a fragment. */
fun <T, A> buildListLayoutHost(
    dataManager: A,
    stateLayout: View,
    refreshLayout: View? = null,
    config: ListLayoutHostConfig.() -> Unit
): ListLayoutHost<T> where A : DataManager<T>, A : Adapter<*> {

    val stateLayoutImpl = (stateLayout as? StateLayout) ?: throw IllegalStateException("Make sure that stateLayout implements StateLayout.")

    val refreshLayoutImpl = if (refreshLayout != null) {
        RefreshViewFactory.createRefreshView(refreshLayout)
    } else {
        null
    }

    val hostConfig = ListLayoutHostConfig().apply(config)

    val loadMore = if (hostConfig.enableLoadMore) {
        WrapperAdapter.wrap(dataManager, hostConfig.triggerLoadMoreByScroll).also {
            hostConfig.onLoadMoreCreated?.invoke(it)
        }
    } else {
        null
    }

    refreshLayoutImpl?.setRefreshHandler(object : RefreshView.RefreshHandler() {
        override fun onRefresh() {
            hostConfig.onRefresh?.invoke()
        }

        override fun canRefresh(): Boolean {
            return if (loadMore != null) {
                !loadMore.isLoadingMore
            } else true
        }
    })

    stateLayoutImpl.stateLayoutConfig.setStateRetryListener(object : OnRetryActionListener {
        override fun onRetry(state: Int) {
            hostConfig.onRetry?.invoke(state)
        }
    })

    var enableLoadMore = loadMore != null

    loadMore?.setOnLoadMoreListener(object : OnLoadMoreListener {
        override fun onLoadMore() {
            hostConfig.onLoadMore?.invoke()
        }

        override fun canLoadMore() = if (refreshLayoutImpl != null) {
            !refreshLayoutImpl.isRefreshing && enableLoadMore
        } else {
            enableLoadMore
        }
    })

    return object : ListLayoutHost<T> {

        val pager = AutoPaging(this, dataManager)

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
            return pager
        }

        override fun isEmpty(): Boolean {
            return dataManager.isEmpty()
        }

        override fun isLoadingMore(): Boolean {
            return loadMore?.isLoadingMore ?: false
        }

        override var isLoadMoreEnable: Boolean
            get() = loadMore != null && enableLoadMore
            set(value) {
                enableLoadMore = value
            }

        override fun autoRefresh() {
            refreshLayoutImpl?.autoRefresh()
        }

        override fun refreshCompleted() {
            refreshLayoutImpl?.refreshCompleted()
        }

        override fun isRefreshing(): Boolean {
            return refreshLayoutImpl?.isRefreshing ?: false
        }

        override var isRefreshEnable: Boolean
            get() = refreshLayoutImpl?.isRefreshEnable ?: false
            set(value) {
                refreshLayoutImpl?.isRefreshEnable = value
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

    }//object end.

}