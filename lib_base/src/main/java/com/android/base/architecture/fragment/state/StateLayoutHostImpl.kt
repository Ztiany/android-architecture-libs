package com.android.base.architecture.fragment.state

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import com.android.base.architecture.ui.CommonId
import com.android.base.architecture.ui.list.RefreshView
import com.android.base.architecture.ui.list.RefreshViewFactory
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayout
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.architecture.ui.state.StateLayoutConfig.RetryableState
import com.android.base.architecture.ui.state.StateLayoutHost
import com.android.base.widget.StateProcessor

/**
 * @author Ztiany
 */
internal class StateLayoutHostImpl constructor(layoutView: View) : StateLayoutHost, StateLayoutConfig {

    private var multiStateView: StateLayout? = null

    private var refreshView: RefreshView? = null

    private var refreshHandler: RefreshView.RefreshHandler? = null

    fun setRefreshHandler(refreshHandler: RefreshView.RefreshHandler?) {
        this.refreshHandler = refreshHandler
    }

    fun setStateRetryListenerUnchecked(retryActionListener: OnRetryActionListener) {
        if (multiStateView != null) {
            setStateRetryListener(retryActionListener)
        }
    }

    private fun setupBaseUiLogic(layoutView: View) {
        multiStateView = layoutView.findViewById<View>(CommonId.STATE_ID) as StateLayout
    }

    private fun setupRefreshLogic(layoutView: View) {
        val refreshLayout = layoutView.findViewById<View>(CommonId.REFRESH_ID) ?: return

        RefreshViewFactory.createRefreshView(refreshLayout).apply {
            setRefreshHandler(object : RefreshView.RefreshHandler() {
                override fun canRefresh(): Boolean {
                    return refreshHandler?.canRefresh() ?: false
                }

                override fun onRefresh() {
                    refreshHandler?.onRefresh()
                }
            })

            refreshView = this
        }
    }

    override fun autoRefresh() {
        refreshView?.autoRefresh()
    }

    override fun showLoadingLayout() {
        checkMultiStateView().showLoadingLayout()
    }

    override fun showContentLayout() {
        refreshCompleted()
        checkMultiStateView().showContentLayout()
    }

    override fun showEmptyLayout() {
        refreshCompleted()
        checkMultiStateView().showEmptyLayout()
    }

    override fun showErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showErrorLayout()
    }

    override fun showRequesting() {
        checkMultiStateView().showRequesting()
    }

    override fun showBlank() {
        checkMultiStateView().showBlank()
    }

    override fun showNetErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showNetErrorLayout()
    }

    override fun showServerErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showServerErrorLayout()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig {
        checkMultiStateView()
        return this
    }

    override fun currentStatus(): Int {
        return multiStateView!!.currentStatus()
    }

    override fun refreshCompleted() {
        if (refreshView != null) {
            refreshView!!.refreshCompleted()
        }
    }

    override fun isRefreshing(): Boolean {
        return refreshView?.isRefreshing ?: false
    }

    override fun isRefreshEnable(): Boolean {
        val refreshView = refreshView
        return refreshView != null && refreshView.isRefreshEnable
    }

    override fun setStateMessage(@RetryableState state: Int, message: CharSequence): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateMessage(state, message)
        return this
    }

    override fun setStateIcon(@RetryableState state: Int, drawable: Drawable): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawable)
        return this
    }

    override fun setStateIcon(@RetryableState state: Int, @DrawableRes drawableId: Int): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawableId)
        return this
    }

    override fun setStateAction(@RetryableState state: Int, actionText: CharSequence): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateAction(state, actionText)
        return this
    }

    override fun setStateRetryListener(retryActionListener: OnRetryActionListener): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateRetryListener(retryActionListener)
        return this
    }

    override fun disableOperationWhenRequesting(disable: Boolean): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.disableOperationWhenRequesting(disable)
        return this
    }

    override fun getProcessor(): StateProcessor {
        return checkMultiStateView().stateLayoutConfig.processor
    }

    private fun checkMultiStateView(): StateLayout {
        return checkNotNull(multiStateView) { "Calling this function requires defining a view that implements StateLayout in the Layout" }
    }

    fun setRefreshEnable(enable: Boolean) {
        refreshView?.isRefreshEnable = enable
    }

    init {
        setupBaseUiLogic(layoutView)
        setupRefreshLogic(layoutView)
    }

}