package com.android.base.app.fragment

import android.graphics.drawable.Drawable
import android.view.View
import com.android.base.app.ui.*
import com.android.base.widget.StateProcessor

internal class RefreshLoadMoreStateLayoutImpl private constructor(layout: View) : StateLayout, StateLayoutConfig {

    companion object {
        fun init(view: View): RefreshLoadMoreStateLayoutImpl {
            return RefreshLoadMoreStateLayoutImpl(view)
        }
    }

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

    override fun setMessageGravity(state: Int, gravity: Int): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setMessageGravity(state, gravity)
        return this
    }

    private fun checkMultiStateView(): StateLayout {
        return _multiStateView
                ?: throw IllegalStateException("Calling this function requires defining a view that implements StateLayout in the Layout")
    }

}