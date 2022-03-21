package com.android.base.architecture.ui.state

interface StateLayoutHost : StateLayout {

    fun autoRefresh()

    fun refreshCompleted()

    fun isRefreshing(): Boolean

    var isRefreshEnable: Boolean

}