package com.android.base.architecture.fragment.list2

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.architecture.fragment.base.BaseUIDialogFragment
import com.android.base.architecture.ui.CommonId
import com.android.base.architecture.ui.list.ListLayoutHost
import com.android.base.architecture.ui.list.Paging
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.foundation.adapter.DataManager
import kotlin.properties.Delegates

/**
 *@author Ztiany
 *@see [BaseList2Fragment]
 */
abstract class BaseList2DialogFragment<T, VB : ViewBinding> : BaseUIDialogFragment<VB>(), ListLayoutHost<T> {

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
    protected fun setUpList(
        dataManager: DataManager<T>
    ): ListLayoutHost<T> {
        return buildListLayoutHost2(
            dataManager,
            vb.root.findViewById(CommonId.STATE_ID),
            vb.root.findViewById(CommonId.REFRESH_ID)
        ) {
            this.onLoadMore = {
                this@BaseList2DialogFragment.onLoadMore()
            }
            this.onRefresh = {
                this@BaseList2DialogFragment.onRefresh()
            }
            this.onRetry = {
                this@BaseList2DialogFragment.onRetry(it)
            }
        }
    }

    protected open fun onRetry(@StateLayoutConfig.RetryableState state: Int) {
        if (!isRefreshing()) {
            autoRefresh()
        }
    }

    protected open fun onRefresh() = onStartLoad()

    protected open fun onLoadMore() = onStartLoad()

    /** called by [onRefresh] or [onLoadMore], you can get current loading type from [isRefreshing] or [isLoadingMore]. */
    protected open fun onStartLoad() {}

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    override fun replaceData(data: List<T>) {
        listLayoutHostImpl.replaceData(data)
    }

    override fun addData(data: List<T>) {
        listLayoutHostImpl.addData(data)
    }

    override fun isEmpty(): Boolean {
        return listLayoutHostImpl.isEmpty()
    }

    override fun isLoadingMore(): Boolean {
        return listLayoutHostImpl.isLoadingMore()
    }

    override fun getPager(): Paging {
        return listLayoutHostImpl.getPager()
    }

    override fun loadMoreCompleted(hasMore: Boolean) =
        listLayoutHostImpl.loadMoreCompleted(hasMore)

    override fun loadMoreFailed() = listLayoutHostImpl.loadMoreFailed()

    override fun isRefreshing() = listLayoutHostImpl.isRefreshing()

    override var isRefreshEnable: Boolean
        get() = listLayoutHostImpl.isRefreshEnable
        set(value) {
            listLayoutHostImpl.isRefreshEnable = value
        }

    override var isLoadMoreEnable: Boolean
        get() = listLayoutHostImpl.isLoadMoreEnable
        set(value) {
            listLayoutHostImpl.isLoadMoreEnable = value
        }

    override fun showContentLayout() = listLayoutHostImpl.showContentLayout()

    override fun showLoadingLayout() = listLayoutHostImpl.showLoadingLayout()

    override fun refreshCompleted() = listLayoutHostImpl.refreshCompleted()

    override fun showEmptyLayout() = listLayoutHostImpl.showEmptyLayout()

    override fun showErrorLayout() = listLayoutHostImpl.showErrorLayout()

    override fun showRequesting() = listLayoutHostImpl.showRequesting()

    override fun showBlank() = listLayoutHostImpl.showBlank()

    override fun getStateLayoutConfig(): StateLayoutConfig = listLayoutHostImpl.stateLayoutConfig

    override fun autoRefresh() = listLayoutHostImpl.autoRefresh()

    override fun showNetErrorLayout() = listLayoutHostImpl.showNetErrorLayout()

    override fun showServerErrorLayout() = listLayoutHostImpl.showServerErrorLayout()

    @StateLayoutConfig.ViewState
    override fun currentStatus() = listLayoutHostImpl.currentStatus()

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