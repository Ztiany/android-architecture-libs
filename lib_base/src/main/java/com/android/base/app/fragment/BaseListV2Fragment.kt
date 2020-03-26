package com.android.base.app.fragment

import android.os.Bundle
import android.view.View
import com.android.base.adapter.DataManager
import com.android.base.app.ui.AutoPageNumber
import com.android.base.app.ui.RefreshListLayout
import com.android.base.app.ui.StateLayoutConfig


/**
 * 区别于 [BaseListFragment] 只能支持 RecyclerView。[BaseListFragment] 采用包装 [androidx.recyclerview.widget.RecyclerView.Adapter] 的方式，
 * 在底部添加 load more view 的 item，来实现加载更多。[BaseListV2Fragment] 没有采用此种方式，所以你使用的 RefreshView 应该是支持这下来刷新和加载更多功能的。
 *
 * 在调用 [BaseListV2Fragment] 的 [onActivityCreated] 之前，你应该设置好 [dataManager]。
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-26 15:06
 */
abstract class BaseListV2Fragment<T> : BaseFragment(), RefreshListLayout<T> {

    private lateinit var stateLayout: RefreshLoadMoreStateLayoutImpl

    protected open lateinit var dataManager: DataManager<T>

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        stateLayout = RefreshLoadMoreStateLayoutImpl.init(view)
        stateLayout.refreshView.setRefreshHandler {
            onRefresh()
        }
        stateLayout.refreshView.setLoadMoreHandler {
            onLoadMore()
        }
        stateLayout.setStateRetryListener(this::onRetry)
    }

    protected open fun onRetry(state: Int) {
        if (!isRefreshing) {
            autoRefresh()
        }
    }

    protected open fun onRefresh() = onStartLoad()

    protected open fun onLoadMore() = onStartLoad()

    /** call by [onRefresh] or [onLoadMore], you can get current loading type from [isRefreshing] or [isLoadingMore]. */
    protected open fun onStartLoad() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!::dataManager.isInitialized) {
            throw NullPointerException("you need set DataManager")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    override fun replaceData(data: MutableList<T>?) {
        dataManager.replaceAll(data)
    }

    override fun addData(data: MutableList<T>?) {
        dataManager.addItems(data)
    }

    fun setRefreshEnable(enable: Boolean) = stateLayout.refreshView.setRefreshEnable(enable)

    fun setLoadMoreEnable(enable: Boolean) = stateLayout.refreshView.setLoadMoreEnable(enable)

    override fun getPager() = AutoPageNumber(this, dataManager)

    override fun isEmpty() = dataManager.isEmpty

    override fun loadMoreCompleted(hasMore: Boolean) = stateLayout.refreshView.loadMoreCompleted(hasMore)

    override fun loadMoreFailed() = stateLayout.refreshView.loadMoreFailed()

    override fun isRefreshing() = stateLayout.refreshView.isRefreshing

    override fun showContentLayout() = stateLayout.showContentLayout()

    override fun showLoadingLayout() = stateLayout.showLoadingLayout()

    override fun refreshCompleted() = stateLayout.refreshView.refreshCompleted()

    override fun showEmptyLayout() = stateLayout.showEmptyLayout()

    override fun showErrorLayout() = stateLayout.showErrorLayout()

    override fun showRequesting() = stateLayout.showRequesting()

    override fun showBlank() = stateLayout.showBlank()

    override fun getStateLayoutConfig() = stateLayout.stateLayoutConfig

    override fun autoRefresh() = stateLayout.refreshView.autoRefresh()

    override fun showNetErrorLayout() = stateLayout.showNetErrorLayout()

    override fun showServerErrorLayout() = stateLayout.showServerErrorLayout()

    override fun isLoadingMore() = stateLayout.refreshView.isLoadingMore

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