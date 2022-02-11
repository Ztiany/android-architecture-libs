package com.android.base.architecture.ui

/**
 * <br></br>   对下拉刷新的抽象
 * <br></br>   Email: 1169654504@qq.com
 *
 * @author Ztiany
 * @version 1.0
 */
interface RefreshView {

    fun autoRefresh()

    fun refreshCompleted()

    fun setRefreshHandler(refreshHandler: RefreshHandler?)

    val isRefreshing: Boolean

    var isRefreshEnable: Boolean

    abstract class RefreshHandler {

        open fun canRefresh(): Boolean {
            return true
        }

        abstract fun onRefresh()
    }

}