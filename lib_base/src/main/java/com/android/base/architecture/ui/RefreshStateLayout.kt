package com.android.base.architecture.ui

interface RefreshStateLayout : StateLayout {

    fun autoRefresh()

    fun refreshCompleted()

    fun isRefreshing(): Boolean

    fun isRefreshEnable(): Boolean

}