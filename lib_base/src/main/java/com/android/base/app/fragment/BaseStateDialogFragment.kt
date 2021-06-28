package com.android.base.app.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.base.app.ui.RefreshStateLayout
import com.android.base.app.ui.RefreshView
import com.android.base.app.ui.StateLayoutConfig
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise

/**
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 * @see BaseStateFragment
 */
abstract class BaseStateDialogFragment : BaseUIDialogFragment(), RefreshStateLayout {

    private lateinit var stateLayout: RefreshableStateLayoutImpl

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        stateLayout = RefreshableStateLayoutImpl.init(view)
        stateLayout.setRefreshHandler(object : RefreshView.RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateDialogFragment.onRefresh()
            }

            override fun canRefresh(): Boolean {
                return this@BaseStateDialogFragment.canRefresh()
            }
        })
        stateLayout.setStateRetryListenerUnchecked { state -> onRetry(state) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshCompleted()
    }

    internal open fun canRefresh() = true

    private val refreshView: RefreshView?
        get() = stateLayout.refreshView

    protected open fun onRetry(@StateLayoutConfig.RetryableState state: Int) {
        refreshView.ifNonNull {
            if (!this.isRefreshing) {
                autoRefresh()
            }
        } otherwise {
            onRefresh()
        }
    }

    protected open fun onRefresh() {}

    fun setRefreshEnable(enable: Boolean) = refreshView?.setRefreshEnable(enable)

    override fun getStateLayoutConfig(): StateLayoutConfig = stateLayout.stateLayoutConfig

    override fun isRefreshing() = stateLayout.isRefreshing

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

    companion object {
        const val CONTENT = StateLayoutConfig.CONTENT
        const val LOADING = StateLayoutConfig.LOADING
        const val ERROR = StateLayoutConfig.ERROR
        const val EMPTY = StateLayoutConfig.EMPTY
        const val NET_ERROR = StateLayoutConfig.NET_ERROR
        const val SERVER_ERROR = StateLayoutConfig.SERVER_ERROR
    }

}