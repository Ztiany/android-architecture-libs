package com.android.base.architecture.fragment.list

import android.graphics.drawable.Drawable
import android.view.View
import com.android.base.architecture.ui.*
import com.android.base.widget.StateProcessor

internal class RefreshLoadMoreStateLayoutImpl constructor(layout: View) : StateLayout, StateLayoutConfig {

    private var _multiStateView: StateLayout? = layout.findViewById<View>(CommonId.STATE_ID) as? StateLayout

    private var _refreshView: RefreshLoadMoreView

    val refreshView: RefreshLoadMoreView
        get() = _refreshView

    init {
        val refreshLayout = layout.findViewById<View>(CommonId.REFRESH_ID)
            ?: throw NullPointerException("You need to provide a refreshable layout with id R.id.base_refresh_layout in your xml.")

        _refreshView = RefreshLoadViewFactory.createRefreshView(refreshLayout)
    }

    override fun showLoadingLayout() = checkMultiStateView().showLoadingLayout()

    override fun showContentLayout() = checkMultiStateView().showContentLayout()

    override fun showEmptyLayout() = checkMultiStateView().showEmptyLayout()

    override fun showErrorLayout() = checkMultiStateView().showErrorLayout()

    override fun showRequesting() = checkMultiStateView().showRequesting()

    override fun showBlank() = checkMultiStateView().showBlank()

    override fun showNetErrorLayout() = checkMultiStateView().showNetErrorLayout()

    override fun showServerErrorLayout() = checkMultiStateView().showServerErrorLayout()

    override fun currentStatus() = checkMultiStateView().currentStatus()

    override fun getStateLayoutConfig(): StateLayoutConfig = checkMultiStateView().stateLayoutConfig

    override fun setStateMessage(state: Int, message: CharSequence?): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateMessage(state, message)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun setStateIcon(state: Int, drawable: Drawable?): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawable)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun setStateIcon(state: Int, drawableId: Int): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawableId)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun setStateAction(state: Int, actionText: CharSequence?): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateAction(state, actionText)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun setStateRetryListener(retryActionListener: OnRetryActionListener?): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateRetryListener(retryActionListener)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun disableOperationWhenRequesting(disable: Boolean): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.disableOperationWhenRequesting(disable)
        return checkMultiStateView().stateLayoutConfig
    }

    override fun getProcessor(): StateProcessor {
        return checkMultiStateView().stateLayoutConfig.processor
    }

    private fun checkMultiStateView(): StateLayout {
        return checkNotNull(_multiStateView) {
            "Calling this function requires defining a view that implements StateLayout in the Layout"
        }
    }

    fun isLoadingMore(): Boolean {
        return refreshView.isLoadingMore()
    }

    fun isRefreshing(): Boolean {
        return refreshView.isRefreshing()
    }

    fun isRefreshEnable(): Boolean {
        return refreshView.isRefreshEnable
    }

    fun isLoadMoreEnable(): Boolean {
        return refreshView.isLoadMoreEnable
    }

    fun setRefreshEnable(enable: Boolean) {
        refreshView.isRefreshEnable = enable
    }

    fun setLoadMoreEnable(enable: Boolean) {
        refreshView.isLoadMoreEnable = enable
    }

}