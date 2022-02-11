package com.android.base.architecture.fragment.list

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import com.android.base.architecture.fragment.state.BaseStateDialogFragment
import com.android.base.architecture.ui.AutoPaging
import com.android.base.architecture.ui.Paging
import com.android.base.architecture.ui.RefreshListLayout
import com.android.base.foundation.adapter.DataManager
import com.ztiany.loadmore.adapter.LoadMore
import com.ztiany.loadmore.adapter.OnLoadMoreListener
import com.ztiany.loadmore.adapter.WrapperAdapter
import kotlin.properties.Delegates

/**
 * @author Ztiany
 * date : 2016-03-19 23:09
 * email: 1169654504@qq.com
 *@see [BaseListFragment]
 */
abstract class BaseListDialogFragment<T, VB : ViewBinding> : BaseStateDialogFragment<VB>(), RefreshListLayout<T> {

    /**加载更多*/
    private var loadMore: LoadMore? = null

    /**列表数据管理*/
    protected var dataManager: DataManager<T> by Delegates.notNull()

    /**分页页码*/
    private val paging by lazy { AutoPaging(this, dataManager) }

    protected fun setupLoadMore(
        recyclerAdapter: Adapter<*>,
        triggerByScroll: Boolean = false
    ): Adapter<*> {
        return WrapperAdapter.wrap(recyclerAdapter, triggerByScroll).apply {
            setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
                    this@BaseListDialogFragment.onLoadMore()
                }

                override fun canLoadMore(): Boolean {
                    return !isRefreshing()
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
        return paging
    }

    val loadMoreController: LoadMore
        get() = loadMore ?: throw NullPointerException("you need to call setupLoadMore first.")

    override fun loadMoreCompleted(hasMore: Boolean) {
        loadMore?.loadCompleted(hasMore)
    }

    override fun loadMoreFailed() {
        loadMore?.loadFail()
    }

}