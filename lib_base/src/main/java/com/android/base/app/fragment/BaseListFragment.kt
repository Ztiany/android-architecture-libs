package com.android.base.app.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import com.android.base.app.ui.AutoPaging
import com.android.base.app.ui.Paging
import com.android.base.app.ui.RefreshListLayout
import com.android.base.foundation.adapter.DataManager
import com.ztiany.loadmore.adapter.ILoadMore
import com.ztiany.loadmore.adapter.OnLoadMoreListener
import com.ztiany.loadmore.adapter.WrapperAdapter

/**
 * 通用的基于 RecyclerView 的列表界面，支持下拉刷新和加载更多。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 */
abstract class BaseListFragment<T, VB : ViewBinding> : BaseStateFragment<VB>(), RefreshListLayout<T> {

    /**加载更多*/
    private var loadMore: ILoadMore? = null

    /**列表数据管理*/
    private lateinit var dataManager: DataManager<T>

    /**分页页码*/
    private var paging: Paging? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!::dataManager.isInitialized) {
            throw NullPointerException("you need to set DataManager")
        }
    }

    protected fun setDataManager(dataManager: DataManager<T>) {
        this.dataManager = dataManager
    }

    protected fun setupLoadMore(
            recyclerAdapter: Adapter<*>,
            standardPagingNumber: Boolean = true,
            paging: Paging = AutoPaging(this, dataManager)
    ): Adapter<*> {
        this.paging = paging

        return WrapperAdapter.wrap(recyclerAdapter, !standardPagingNumber).apply {
            setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
                    this@BaseListFragment.onLoadMore()
                }

                override fun canLoadMore(): Boolean {
                    return !isRefreshing
                }
            })
            loadMore = this
        }
    }

    /**call by [.onRefresh] or [.onLoadMore], you can get current loading type from [.isRefreshing] or [.isLoadingMore].*/
    protected open fun onStartLoad() {}

    override fun onRefresh() = onStartLoad()

    protected fun onLoadMore() = onStartLoad()

    override fun canRefresh() = !isLoadingMore()

    override fun replaceData(data: List<T>) = dataManager.replaceAll(data)

    override fun addData(data: List<T>) = dataManager.addItems(data)

    override fun isEmpty(): Boolean {
        return dataManager.isEmpty()
    }

    override fun isLoadingMore(): Boolean {
        return loadMore != null && loadMore?.isLoadingMore ?: false
    }

    override fun getPager(): Paging {
        return paging ?: throw NullPointerException("you need to call setupLoadMore first")
    }

    val loadMoreController: ILoadMore
        get() = loadMore ?: throw NullPointerException("you need to call setupLoadMore first")

    override fun loadMoreCompleted(hasMore: Boolean) {
        loadMore?.loadCompleted(hasMore)
    }

    override fun loadMoreFailed() {
        loadMore?.loadFail()
    }

}