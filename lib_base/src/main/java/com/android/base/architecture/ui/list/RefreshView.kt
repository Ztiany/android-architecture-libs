package com.android.base.architecture.ui.list

/**
 *对下拉刷新的抽象
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