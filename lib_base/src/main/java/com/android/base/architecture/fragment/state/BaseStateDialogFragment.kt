package com.android.base.architecture.fragment.state

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.architecture.fragment.base.BaseUIDialogFragment
import com.android.base.architecture.ui.list.RefreshView
import com.android.base.architecture.ui.state.OnRetryActionListener
import com.android.base.architecture.ui.state.StateLayoutConfig
import com.android.base.architecture.ui.state.StateLayoutHost

/**
 * @author Ztiany
 * Date :   2016-03-19 23:09
 * @see BaseStateFragment
 */
abstract class BaseStateDialogFragment<VB : ViewBinding> : BaseUIDialogFragment<VB>(), StateLayoutHost {

    private lateinit var mStateLayout: StateLayoutHostImpl

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        mStateLayout = StateLayoutHostImpl(view)

        mStateLayout.setRefreshHandler(object : RefreshView.RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateDialogFragment.onRefresh()
            }

            override fun canRefresh(): Boolean {
                return this@BaseStateDialogFragment.canRefresh()
            }
        })

        mStateLayout.setStateRetryListenerUnchecked(object : OnRetryActionListener {
            override fun onRetry(state: Int) {
                this@BaseStateDialogFragment.onRetry(state)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    internal open fun canRefresh() = true

    protected open fun onRetry(@StateLayoutConfig.RetryableState state: Int) {
        if (mStateLayout.isRefreshEnable()) {
            if (!isRefreshing()) {
                mStateLayout.autoRefresh()
            }
        } else {
            onRefresh()
        }
    }

    protected open fun onRefresh() {}

    fun setRefreshEnable(enable: Boolean) {
        mStateLayout.setRefreshEnable(enable)
    }

    override fun isRefreshEnable(): Boolean {
        return mStateLayout.isRefreshEnable()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig = mStateLayout.stateLayoutConfig

    override fun isRefreshing() = mStateLayout.isRefreshing()

    override fun refreshCompleted() = mStateLayout.refreshCompleted()

    override fun autoRefresh() = mStateLayout.autoRefresh()

    override fun showContentLayout() = mStateLayout.showContentLayout()

    override fun showLoadingLayout() = mStateLayout.showLoadingLayout()

    override fun showEmptyLayout() = mStateLayout.showEmptyLayout()

    override fun showErrorLayout() = mStateLayout.showErrorLayout()

    override fun showRequesting() = mStateLayout.showRequesting()

    override fun showBlank() = mStateLayout.showBlank()

    override fun showNetErrorLayout() = mStateLayout.showNetErrorLayout()

    override fun showServerErrorLayout() = mStateLayout.showServerErrorLayout()

    override fun currentStatus() = mStateLayout.currentStatus()

    @Suppress("UNUSED")
    companion object {
        const val CONTENT = StateLayoutConfig.CONTENT
        const val LOADING = StateLayoutConfig.LOADING
        const val ERROR = StateLayoutConfig.ERROR
        const val EMPTY = StateLayoutConfig.EMPTY
        const val NET_ERROR = StateLayoutConfig.NET_ERROR
        const val SERVER_ERROR = StateLayoutConfig.SERVER_ERROR
    }

}