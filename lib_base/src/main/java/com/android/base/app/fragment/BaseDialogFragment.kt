package com.android.base.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatDialogFragment
import com.android.base.app.Sword
import com.android.base.app.activity.BackHandlerHelper
import com.android.base.app.activity.OnBackPressListener
import com.android.base.app.fragment.delegates.FragmentDelegate
import com.android.base.app.fragment.delegates.FragmentDelegateOwner
import com.android.base.app.ui.LoadingView
import com.android.base.rx.autodispose.AutoDisposeLifecycleOwnerEx
import com.github.dmstocking.optional.java.util.function.Predicate
import timber.log.Timber

/**
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 * @see [BaseFragment]
 */
open class BaseDialogFragment : AppCompatDialogFragment(), LoadingView, OnBackPressListener, FragmentDelegateOwner, AutoDisposeLifecycleOwnerEx {

    private var loadingView: LoadingView? = null

    private var layoutView: View? = null

    /** just for cache*/
    private var cachedView: View? = null

    @Suppress("LeakingThis")
    private val fragmentDelegates = FragmentDelegates(this)

    private var recentShowingDialogTime: Long = 0

    private var _state: Bundle? = null

    protected val state: Bundle
        get() {
            var state = _state
            if (state == null) {
                state = Bundle()
                _state = state
            }
            return state
        }

    private fun tag() = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(tag()).d("onAttach() called with: context = [$context]")
        fragmentDelegates.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _state = getInstanceState(savedInstanceState)
        Timber.tag(tag()).d("-->onCreate  savedInstanceState   =   $savedInstanceState")
        fragmentDelegates.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (cachedView == null) {
            val layout = provideLayout() ?: return null
            if (layout is Int) {
                return inflater.inflate(layout, container, false).also { cachedView = it }
            }
            if (layout is View) {
                cachedView = layout
                return layout
            }
            throw IllegalArgumentException("Here you should provide  a  layout id  or a View")
        }

        Timber.tag(tag()).d("mCachedView.parent: " + cachedView?.parent)

        cachedView?.run {
            val viewParent = parent
            if (viewParent != null && viewParent is ViewGroup) {
                viewParent.removeView(this)
            }
        }

        return cachedView
    }

    /**
     * 使用此方法提供的布局，将只会被缓存起来，即此方法将只会被调用一次。
     *
     * @return provide  a  layout id  or a View
     */
    protected open fun provideLayout(): Any? = null

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(tag()).d("-->onViewCreated  savedInstanceState = %s", savedInstanceState)
        if (layoutView !== view) {
            layoutView = view
            internalOnViewPrepared(view, savedInstanceState)
            onViewPrepared(view, savedInstanceState)
        }
        fragmentDelegates.onViewCreated(view, savedInstanceState)
    }

    internal open fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {}

    /**
     * View is prepared, If [androidx.fragment.app.Fragment.onCreateView] return same layout, it will be called once
     *
     * @param view view of fragment
     */
    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.tag(tag()).d("-->onActivityCreated savedInstanceState   =   $savedInstanceState")
        fragmentDelegates.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(tag()).d("-->onStart")
        fragmentDelegates.onStart()
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(tag()).d("-->onResume")
        fragmentDelegates.onResume()
    }

    override fun onPause() {
        Timber.tag(tag()).d("-->onPause")
        fragmentDelegates.onPause()
        super.onPause()
    }

    override fun onStop() {
        Timber.tag(tag()).d("-->onStop")
        fragmentDelegates.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.tag(tag()).d("-->onDestroyView")
        fragmentDelegates.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.tag(tag()).d("-->onDestroy")
        fragmentDelegates.onDestroy()
        super.onDestroy()
        dismissLoadingDialog()
    }

    override fun onDetach() {
        Timber.tag(tag()).d("-->onDetach")
        fragmentDelegates.onDetach()
        super.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        fragmentDelegates.onSaveInstanceState(outState)
        saveInstanceState(outState, _state)
        super.onSaveInstanceState(outState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Timber.tag(tag()).d("-->setUserVisibleHint ==$isVisibleToUser")
        fragmentDelegates.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.tag(tag()).d("-->onHiddenChanged = $hidden")
        fragmentDelegates.onHiddenChanged(hidden)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentDelegates.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentDelegates.onActivityResult(requestCode, resultCode, data)
    }

    @UiThread
    override fun addDelegate(fragmentDelegate: FragmentDelegate<*>?) {
        fragmentDelegates.addDelegate(fragmentDelegate)
    }

    @UiThread
    override fun removeDelegate(fragmentDelegate: FragmentDelegate<*>?): Boolean {
        return fragmentDelegates.removeDelegate(fragmentDelegate)
    }

    override fun findDelegate(predicate: Predicate<FragmentDelegate<*>?>?): FragmentDelegate<*>? {
        return fragmentDelegates.findDelegate(predicate)
    }

    final override fun onBackPressed(): Boolean {
        return handleBackPress() || BackHandlerHelper.handleBackPress(this)
    }

    /**
     * Fragment需要自己处理BackPress事件，如果不处理，就交给子Fragment处理。都不处理则由Activity处理
     */
    protected open fun handleBackPress(): Boolean {
        return false
    }

    private fun loadingView(): LoadingView {
        val loadingViewImpl = loadingView
        return if (loadingViewImpl != null) {
            loadingViewImpl
        } else {
            loadingView = onCreateLoadingView()
                    ?: Sword.get().loadingViewFactory.createLoadingDelegate(requireContext())
            loadingView
                    ?: throw NullPointerException("you need to config LoadingViewFactory")
        }
    }

    protected open fun onCreateLoadingView(): LoadingView? {
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
        loadingView().dismissLoadingDialog()
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: () -> Unit) {
        dismissDialog(recentShowingDialogTime, minimumMills, onDismiss)
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingView().isLoadingDialogShowing()
    }

    override fun showMessage(message: CharSequence) {
        loadingView().showMessage(message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        loadingView().showMessage(messageId)
    }

}