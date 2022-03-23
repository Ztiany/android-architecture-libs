package com.android.base.architecture.fragment.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.CommonId
import com.android.base.architecture.ui.list.ListLayoutHost
import com.android.base.architecture.ui.list.Paging
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.foundation.adapter.DataManager
import com.ztiany.loadmore.adapter.LoadMore
import kotlin.properties.Delegates

/**
 * 通用的基于 RecyclerView 的列表界面，支持下拉刷新和加载更多。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 */
abstract class BaseListFragment<T, VB : ViewBinding> : BaseUIFragment<VB>(), ListLayoutHost<T> {

    private var loadMore: LoadMore? = null

    private var listLayoutHostImpl: ListLayoutHost<T> by Delegates.notNull()

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.internalOnViewPrepared(view, savedInstanceState)
        listLayoutHostImpl = provideListImplementation(view, savedInstanceState)
    }

    /**
     *  1. This Method will be called before [onViewCreated] and [onViewPrepared].
     *  2. You should invoke [setUpList] to return a real [ListLayoutHost].
     */
    abstract fun provideListImplementation(view: View, savedInstanceState: Bundle?): ListLayoutHost<T>

    @SuppressWarnings("WeakerAccess")
    protected fun <A> setUpList(
        dataManager: A,
        recyclerView: RecyclerView,
        enableLoadMore: Boolean = false,
        triggerLoadMoreByScroll: Boolean = false
    ): ListLayoutHost<T> where A : DataManager<T>, A : Adapter<*> {

        if (!enableLoadMore) {
            recyclerView.adapter = dataManager
        }

        return buildListLayoutHost(
            dataManager,
            vb.root.findViewById(CommonId.STATE_ID),
            vb.root.findViewById(CommonId.REFRESH_ID)
        ) {
            this.enableLoadMore = enableLoadMore
            this.triggerLoadMoreByScroll = triggerLoadMoreByScroll

            /*if load-more is enabled.*/
            onLoadMoreCreated = {
                loadMore = it
                recyclerView.adapter = it
            }

            this.onRetry = {
                this@BaseListFragment.onRetry(it)
            }
            this.onRefresh = {
                this@BaseListFragment.onRefresh()
            }
            this.onLoadMore = {
                this@BaseListFragment.onLoadMore()
            }
        }
    }

    protected open fun onRetry(@StateLayoutConfig.RetryableState state: Int) {
        if (listLayoutHostImpl.isRefreshEnable) {
            if (!isRefreshing()) {
                listLayoutHostImpl.autoRefresh()
            }
        } else {
            onRefresh()
        }
    }

    protected open fun onRefresh() = onStartLoad()

    protected open fun onLoadMore() = onStartLoad()

    /**called by [.onRefresh] or [.onLoadMore], you can get current loading type from [isRefreshing] or [isLoadingMore].*/
    protected open fun onStartLoad() {}

    override fun replaceData(data: List<T>) = listLayoutHostImpl.replaceData(data)

    override fun addData(data: List<T>) = listLayoutHostImpl.addData(data)

    override fun isEmpty(): Boolean {
        return listLayoutHostImpl.isEmpty()
    }

    override fun isLoadingMore(): Boolean {
        return listLayoutHostImpl.isLoadingMore()
    }

    override fun getPager(): Paging {
        return listLayoutHostImpl.getPager()
    }

    val loadMoreController: LoadMore
        get() = loadMore ?: throw NullPointerException("You didn't enable load-more.")

    override fun loadMoreCompleted(hasMore: Boolean) {
        loadMore?.loadCompleted(hasMore)
    }

    override fun loadMoreFailed() {
        loadMore?.loadFail()
    }

    override var isLoadMoreEnable: Boolean
        get() = listLayoutHostImpl.isLoadMoreEnable
        set(value) {
            listLayoutHostImpl.isLoadMoreEnable = value
        }

    override fun autoRefresh() {
        listLayoutHostImpl.autoRefresh()
    }

    override fun refreshCompleted() {
        listLayoutHostImpl.refreshCompleted()
    }

    override fun isRefreshing(): Boolean {
        return listLayoutHostImpl.isRefreshing()
    }

    override fun showContentLayout() {
        listLayoutHostImpl.showContentLayout()
    }

    override fun showLoadingLayout() {
        listLayoutHostImpl.showLoadingLayout()
    }

    override fun showEmptyLayout() {
        listLayoutHostImpl.showEmptyLayout()
    }

    override fun showErrorLayout() {
        listLayoutHostImpl.showErrorLayout()
    }

    override fun showRequesting() {
        listLayoutHostImpl.showRequesting()
    }

    override fun showBlank() {
        listLayoutHostImpl.showBlank()
    }

    override fun showNetErrorLayout() {
        listLayoutHostImpl.showNetErrorLayout()
    }

    override fun showServerErrorLayout() {
        listLayoutHostImpl.showServerErrorLayout()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig {
        return listLayoutHostImpl.stateLayoutConfig
    }

    @StateLayoutConfig.ViewState
    override fun currentStatus(): Int {
        return listLayoutHostImpl.currentStatus()
    }

    override var isRefreshEnable: Boolean
        get() = listLayoutHostImpl.isRefreshEnable
        set(value) {
            listLayoutHostImpl.isRefreshEnable = value
        }

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