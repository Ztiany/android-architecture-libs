package com.android.base.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.android.base.app.activity.ActivityDelegateOwner
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.delegates.FragmentDelegateOwner
import com.android.base.interfaces.ActivityLifecycleCallbacksAdapter
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection

internal class AndroidComponentLifecycleInjector : ActivityLifecycleCallbacksAdapter {

    private var isAutoInjectEnable = false

    var delegateInjector: DelegateInjector? = null

    fun enableAutoInject() {
        isAutoInjectEnable = true
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        handleAutoInjectActivity(activity)
        handleAutoInstallActivityDelegate(activity)
        if (activity is FragmentActivity) {
            injectFragmentLifecycle(activity)
        }
    }

    private fun injectFragmentLifecycle(activity: FragmentActivity) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: FragmentManager, fragment: Fragment, context: Context) {
                handleAutoInjectFragment(fragment)
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

    private fun handleAutoInjectActivity(activity: Activity?) {
        if (isAutoInjectEnable && activity is Injectable) {
            AndroidInjection.inject(activity)
        }
    }

    private fun handleAutoInjectFragment(fragment: Fragment) {
        if (isAutoInjectEnable && fragment is Injectable) {
            AndroidSupportInjection.inject(fragment)
        }
    }

}