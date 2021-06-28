package com.android.base.app.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.app.ui.AutoPaging
import com.android.base.app.ui.Paging
import com.android.base.app.ui.RefreshListLayout
import com.android.base.app.ui.StateLayoutConfig
import com.android.base.foundation.adapter.DataManager

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-26 15:06
 *@see [BaseListV2Fragment]
 */
abstract class BaseListV2DialogFragment<T> : BaseUIDialogFragment(), RefreshListLayout<T> {

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
            throw NullPointerException("you need to set DataManager")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    override fun replaceData(data: List<T>) {
        dataManager.replaceAll(data)
    }

    override fun addData(data: List<T>) {
        dataManager.addItems(data)
    }

    fun setRefreshEnable(enable: Boolean) = stateLayout.refreshView.setRefreshEnable(enable)

    fun setLoadMoreEnable(enable: Boolean) = stateLayout.refreshView.setLoadMoreEnable(enable)

    override fun isEmpty(): Boolean {
        return dataManager.isEmpty()
    }

    override fun isLoadingMore(): Boolean {
        return stateLayout.refreshView.isLoadingMore
    }

    private val _paging: Paging by lazy { AutoPaging(this, dataManager) }

    override fun getPager(): Paging {
        return _paging
    }

    override fun loadMoreCompleted(hasMore: Boolean) =
        stateLayout.refreshView.loadMoreCompleted(hasMore)

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