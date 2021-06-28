package com.android.base.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.android.base.app.Sword
import com.android.base.app.fragment.animator.FragmentAnimatorHelper
import com.android.base.app.ui.LoadingView

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-06 23:00
 */
abstract class BaseUIDialogFragment : BaseDialogFragment(), LoadingView {

    private var fragmentAnimatorHelper: FragmentAnimatorHelper? = null

    private var recentShowingDialogTime: Long = 0

    private var loadingView: LoadingView? = null

    private val reuseView by lazy { ReusableView() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return reuseView.createView(provideLayout(), inflater, container)
    }

    /**
     * @return provide a layout id or a View
     */
    abstract fun provideLayout(): Any?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (reuseView.isNotTheSameView(view)) {
            internalOnViewPrepared(view, savedInstanceState)
            onViewPrepared(view, savedInstanceState)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    internal open fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {}

    /**
     * View is prepared, If [androidx.fragment.app.Fragment.onCreateView] reuse the layout, it will be called once
     *
     * @param view view of fragment
     */
    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}

    /**
     * 1. Call it before [onCreateView] if you want.
     * 2. The reason it exist is that Hilt can not work properly when passing a default value to Fragment's constructor. In details, the default value is commonly a layout id.
     * 3. 缓存 Fragment 的 View，默认为不缓存，可能在某些特点场景下才会需要用到，设置为缓存可能有未知的问题。
     */
    protected fun setReuseView(reuseTheView: Boolean) {
        reuseView.reuseTheView = reuseTheView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        reuseView.destroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }

    protected open fun onCreateLoadingView(): LoadingView? {
        return null
    }

    private fun loadingView(): LoadingView {
        val loadingViewImpl = loadingView
        return if (loadingViewImpl != null) {
            loadingViewImpl
        } else {
            loadingView = onCreateLoadingView()
                ?: Sword.loadingViewFactory?.invoke(requireContext())
            loadingView
                ?: throw NullPointerException("you need to config LoadingViewFactory in Sword or implement onCreateLoadingView.")
        }
    }

    override fun showLoadingDialog() {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(cancelable)
    }

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(message, cancelable)
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(messageId, cancelable)
    }

    override fun dismissLoadingDialog() {
        loadingView?.dismissLoadingDialog()
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        dismissDialog(recentShowingDialogTime, minimumMills, onDismiss)
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingView != null && loadingView().isLoadingDialogShowing()
    }

    override fun showMessage(message: CharSequence) {
        loadingView().showMessage(message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        loadingView().showMessage(messageId)
    }

}