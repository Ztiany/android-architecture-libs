package com.android.base.architecture.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.android.base.AndroidSword
import com.android.base.architecture.fragment.animator.FragmentAnimatorHelper
import com.android.base.architecture.fragment.tools.FragmentConfig
import com.android.base.architecture.fragment.tools.ReusableView
import com.android.base.architecture.fragment.tools.dismissDialog
import com.android.base.architecture.ui.inflateBindingWithParameterizedType
import com.android.base.architecture.ui.loading.LoadingViewHost

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-06 23:00
 */
abstract class BaseUIFragment<VB : ViewBinding> : BaseFragment(), LoadingViewHost {

    private var fragmentAnimatorHelper: FragmentAnimatorHelper? = null

    private var recentShowingDialogTime: Long = 0

    private var mLoadingViewHost: LoadingViewHost? = null

    private val reuseView by lazy { ReusableView() }

    private var _vb: VB? = null
    protected val vb: VB
        get() = checkNotNull(_vb) {
            "access this after onCreateView() is called."
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = {
            _vb = provideViewBinding(inflater, savedInstanceState) ?: inflateBindingWithParameterizedType(layoutInflater, container, false)
            vb.root
        }
        return reuseView.createView(factory)
    }

    protected open fun provideViewBinding(inflater: LayoutInflater, savedInstanceState: Bundle?): VB? = null

    @CallSuper
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
        if (reuseView.destroyView()) {
            _vb = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
        _vb = null
    }

    private fun loadingView(): LoadingViewHost {
        val loadingViewImpl = mLoadingViewHost
        return if (loadingViewImpl != null) {
            loadingViewImpl
        } else {
            mLoadingViewHost = onCreateLoadingView() ?: AndroidSword.sLoadingViewHostFactory?.invoke(requireContext())
            mLoadingViewHost ?: throw NullPointerException("you need to config LoadingViewFactory in Sword or implement onCreateLoadingView.")
        }
    }

    protected open fun onCreateLoadingView(): LoadingViewHost? {
        return null
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
        mLoadingViewHost?.dismissLoadingDialog()
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        dismissDialog(recentShowingDialogTime, minimumMills, onDismiss)
    }

    override fun isLoadingDialogShowing(): Boolean {
        return mLoadingViewHost != null && loadingView().isLoadingDialogShowing()
    }

    override fun showMessage(message: CharSequence) {
        loadingView().showMessage(message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        loadingView().showMessage(messageId)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (fragmentAnimatorHelper == null) {
            fragmentAnimatorHelper = FragmentAnimatorHelper(requireContext(), FragmentConfig.defaultFragmentAnimator())
        }
        return fragmentAnimatorHelper?.onCreateAnimation(transit, enter)
    }

}