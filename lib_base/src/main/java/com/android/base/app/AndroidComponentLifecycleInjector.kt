package com.android.base.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.foundation.fragment.FragmentDelegateOwner
import com.android.base.interfaces.ActivityLifecycleCallbacksAdapter

internal class AndroidComponentLifecycleInjector : ActivityLifecycleCallbacksAdapter {

    var delegateInjector: DelegateInjector? = null

     override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        handleAutoInstallActivityDelegate(activity)
        if (activity is FragmentActivity) {
            injectFragmentLifecycle(activity)
        }
    }

    private fun injectFragmentLifecycle(activity: FragmentActivity) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: FragmentManager, fragment: Fragment, context: Context) {
                handleAutoInstallFragmentDelegate(fragment)
            }
        }, true)
    }

    private fun handleAutoInstallFragmentDelegate(fragment: Fragment) {
        if (fragment is FragmentDelegateOwner) {
            delegateInjector?.injectFragmentDelegate(fragment)
        }
    }

    private fun handleAutoInstallActivityDelegate(activity: Activity?) {
        if (activity is ActivityDelegateOwner) {
            delegateInjector?.injectActivityDelegate(activity)
        }
    }

}