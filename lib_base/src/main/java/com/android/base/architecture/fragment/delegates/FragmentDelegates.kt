package com.android.base.architecture.fragment.delegates

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner
import java.util.*

@UiThread
internal class FragmentDelegates(
    private val delegateOwner: Fragment
) : FragmentDelegate<Fragment>, FragmentDelegateOwner {

    private val delegates: MutableList<FragmentDelegate<*>> = ArrayList(4)

    override fun onAttach(context: Context) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onAttach(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onCreate(savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onViewCreated(view, savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onActivityCreated(savedInstanceState)
        }
    }

    override fun onStart() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onStart()
        }
    }

    override fun onResume() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onResume()
        }
    }

    override fun onPause() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onPause()
        }
    }

    override fun onStop() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onStop()
        }
    }

    override fun onDestroy() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onDestroy()
        }
    }

    override fun onDestroyView() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onDestroyView()
        }
    }

    override fun onDetach() {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onDetach()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.setUserVisibleHint(isVisibleToUser)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onSaveInstanceState(savedInstanceState)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onHiddenChanged(hidden)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (fragmentDelegate in delegates) {
            fragmentDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun addDelegate(fragmentDelegate: FragmentDelegate<*>) {
        delegates.add(fragmentDelegate)
        @Suppress("UNCHECKED_CAST")
        (fragmentDelegate as FragmentDelegate<Fragment>).onAttachToFragment(delegateOwner)
    }

    override fun removeDelegate(fragmentDelegate: FragmentDelegate<*>): Boolean {
        val remove = delegates.remove(fragmentDelegate)
        if (remove) {
            fragmentDelegate.onDetachFromFragment()
        }
        return remove
    }

    override fun findDelegate(predicate: (FragmentDelegate<*>) -> Boolean): FragmentDelegate<*>? {
        for (delegate in delegates) {
            if (predicate(delegate)) {
                return delegate
            }
        }
        return null
    }

}