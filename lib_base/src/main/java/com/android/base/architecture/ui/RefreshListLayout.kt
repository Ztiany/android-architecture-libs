package com.android.base.architecture.ui

/**
 * 列表视图行为
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-03-29 22:16
 */
interface RefreshListLayout<T> : RefreshStateLayout {

    fun replaceData(data: List<T>)

    fun addData(data: List<T>)

    fun loadMoreCompleted(hasMore: Boolean)

    fun loadMoreFailed()

    fun getPager(): Paging

    fun isEmpty(): Boolean

    fun isLoadingMore(): Boolean

    fun isLoadMoreEnable(): Boolean

}