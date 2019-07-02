package com.android.base.app.fragment

import android.graphics.drawable.Drawable
import android.view.View
import com.android.base.app.ui.CommonId
import com.android.base.app.ui.*

internal class RefreshLoadMoreStateLayoutImpl private constructor(private val mLayout: View) : StateLayout, StateLayoutConfig {

    companion object {
        fun init(view: View): RefreshLoadMoreStateLayoutImpl {
            return RefreshLoadMoreStateLayoutImpl(view)
        }
    }

    private var mMultiStateView: StateLayout = mLayout.findViewById<View>(CommonId.STATE_ID) as StateLayout
    private var mRefreshView: RefreshLoadMoreView

    val refreshView: RefreshLoadMoreView
        get() = mRefreshView

    init {
        val refreshLayout = mLayout.findViewById<View>(CommonId.REFRESH_ID)
        mRefreshView = RefreshLoadViewFactory.createRefreshView(refreshLayout)
    }

    override fun showLoadingLayout() = mMultiStateView.showLoadingLayout()
    override fun showContentLayout() = mMultiStateView.showContentLayout()
    override fun showEmptyLayout() = mMultiStateView.showEmptyLayout()
    override fun showErrorLayout() = mMultiStateView.showErrorLayout()
    override fun showRequesting() = mMultiStateView.showRequesting()
    override fun showBlank() = mMultiStateView.showBlank()
    override fun showNetErrorLayout() = mMultiStateView.showNetErrorLayout()
    override fun showServerErrorLayout() = mMultiStateView.showServerErrorLayout()

    override fun getStateLayoutConfig(): StateLayoutConfig = mMultiStateView.stateLayoutConfig

    override fun setStateMessage(state: Int, message: CharSequence?): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.setStateMessage(state, message)
        return mMultiStateView.stateLayoutConfig
    }

    override fun setStateIcon(state: Int, drawable: Drawable?): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.setStateIcon(state, drawable)
        return mMultiStateView.stateLayoutConfig
    }

    override fun setStateIcon(state: Int, drawableId: Int): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.setStateIcon(state, drawableId)
        return mMultiStateView.stateLayoutConfig
    }

    override fun setStateAction(state: Int, actionText: CharSequence?): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.setStateAction(state, actionText)
        return mMultiStateView.stateLayoutConfig
    }

    override fun setStateRetryListener(retryActionListener: OnRetryActionListener?): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.setStateRetryListener(retryActionListener)
        return mMultiStateView.stateLayoutConfig
    }

    override fun disableOperationWhenRequesting(disable: Boolean): StateLayoutConfig {
        mMultiStateView.stateLayoutConfig.disableOperationWhenRequesting(disable)
        return mMultiStateView.stateLayoutConfig
    }

}