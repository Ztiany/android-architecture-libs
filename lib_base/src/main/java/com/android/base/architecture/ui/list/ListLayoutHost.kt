package com.android.base.architecture.ui.list

import com.android.base.architecture.ui.state.StateLayoutHost

/**
 * 列表视图行为。
 *
 * @author Ztiany
 * Date : 2017-03-29 22:16
 */
interface ListLayoutHost<T> : StateLayoutHost {

    fun replaceData(data: List<T>)

    fun addData(data: List<T>)

    fun loadMoreCompleted(hasMore: Boolean)

    fun loadMoreFailed()

    fun getPager(): Paging

    fun isEmpty(): Boolean

    fun isLoadingMore(): Boolean

    var isLoadMoreEnable: Boolean

}