package com.android.base.architecture.fragment.state

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.architecture.fragment.base.BaseUIDialogFragment
import com.android.base.architecture.ui.OnRetryActionListener
import com.android.base.architecture.ui.RefreshStateLayout
import com.android.base.architecture.ui.RefreshView
import com.android.base.architecture.ui.StateLayoutConfig

/**
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 * @see BaseStateFragment
 */
abstract class BaseStateDialogFragment<VB : ViewBinding> : BaseUIDialogFragment<VB>(), RefreshStateLayout {

    private lateinit var stateLayout: RefreshableStateLayoutImpl

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        stateLayout = RefreshableStateLayoutImpl(view)

        stateLayout.setRefreshHandler(object : RefreshView.RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateDialogFragment.onRefresh()
            }

            override fun canRefresh(): Boolean {
                return this@BaseStateDialogFragment.canRefresh()
            }
        })

        stateLayout.setStateRetryListenerUnchecked(object : OnRetryActionListener {
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
        if (stateLayout.isRefreshEnable()) {
            if (!isRefreshing()) {
                stateLayout.autoRefresh()
            }
        } else {
            onRefresh()
        }
    }

    protected open fun onRefresh() {}

    fun setRefreshEnable(enable: Boolean) {
        stateLayout.setRefreshEnable(enable)
    }

    override fun isRefreshEnable(): Boolean {
        return stateLayout.isRefreshEnable()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig = stateLayout.stateLayoutConfig

    override fun isRefreshing() = stateLayout.isRefreshing()

    override fun refreshCompleted() = stateLayout.refreshCompleted()

    override fun autoRefresh() = stateLayout.autoRefresh()

    override fun showContentLayout() = stateLayout.showContentLayout()

    override fun showLoadingLayout() = stateLayout.showLoadingLayout()

    override fun showEmptyLayout() = stateLayout.showEmptyLayout()

    override fun showErrorLayout() = stateLayout.showErrorLayout()

    override fun showRequesting() = stateLayout.showRequesting()

    override fun showBlank() = stateLayout.showBlank()

    override fun showNetErrorLayout() = stateLayout.showNetErrorLayout()

    override fun showServerErrorLayout() = stateLayout.showServerErrorLayout()

    override fun currentStatus() = stateLayout.currentStatus()

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