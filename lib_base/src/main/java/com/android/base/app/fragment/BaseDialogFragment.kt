package com.android.base.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatDialogFragment
import com.android.base.app.activity.BackHandlerHelper
import com.android.base.app.activity.OnBackPressListener
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner
import com.android.base.rx.autodispose.AutoDisposeLifecycleOwnerEx
import timber.log.Timber

/**
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 * @see [BaseFragment]
 */
open class BaseDialogFragment : AppCompatDialogFragment(), OnBackPressListener,
    FragmentDelegateOwner, AutoDisposeLifecycleOwnerEx {

    private val fragmentDelegates by lazy { FragmentDelegates(this) }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(tag()).d("-->onCreateView  savedInstanceState = %s", savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(tag()).d("-->onViewCreated  savedInstanceState = %s", savedInstanceState)
        fragmentDelegates.onViewCreated(view, savedInstanceState)
    }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentDelegates.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentDelegates.onActivityResult(requestCode, resultCode, data)
    }

    @UiThread
    override fun addDelegate(fragmentDelegate: FragmentDelegate<*>) {
        fragmentDelegates.addDelegate(fragmentDelegate)
    }

    @UiThread
    override fun removeDelegate(fragmentDelegate: FragmentDelegate<*>): Boolean {
        return fragmentDelegates.removeDelegate(fragmentDelegate)
    }

    @UiThread
    override fun findDelegate(predicate: (FragmentDelegate<*>) -> Boolean): FragmentDelegate<*>? {
        return fragmentDelegates.findDelegate(predicate)
    }

    final override fun onBackPressed(): Boolean {
        return handleBackPress() || BackHandlerHelper.handleBackPress(this)
    }

    /**
     * Fragment需要自己处理BackPress事件，如果不处理，就交给子Fragment处理，都不处理则由Activity处理。
     */
    protected open fun handleBackPress(): Boolean {
        return false
    }

}