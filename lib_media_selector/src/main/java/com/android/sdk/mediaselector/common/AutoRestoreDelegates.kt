package com.android.sdk.mediaselector.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.base.foundation.activity.ActivityDelegate
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner

internal fun autoCallback(appCompatActivity: AppCompatActivity, stateHandler: ActivityStateHandler) {
    if (appCompatActivity is ActivityDelegateOwner) {
        (appCompatActivity as ActivityDelegateOwner).addDelegate(object : ActivityDelegate<AppCompatActivity> {
            override fun onSaveInstanceState(savedInstanceState: Bundle) {
                stateHandler.onSaveInstanceState(savedInstanceState)
            }

            override fun onRestoreInstanceState(savedInstanceState: Bundle) {
                stateHandler.onRestoreInstanceState(savedInstanceState)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                stateHandler.onActivityResult(requestCode, resultCode, data)
            }
        })
    }
}

internal fun autoCallback(fragment: Fragment, stateHandler: ActivityStateHandler) {
    if (fragment is FragmentDelegateOwner) {
        (fragment as FragmentDelegateOwner).addDelegate(object : FragmentDelegate<Fragment> {
            override fun onActivityCreated(savedInstanceState: Bundle?) {
                stateHandler.onRestoreInstanceState(savedInstanceState)
            }

            override fun onSaveInstanceState(savedInstanceState: Bundle) {
                stateHandler.onSaveInstanceState(savedInstanceState)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                stateHandler.onActivityResult(requestCode, resultCode, data)
            }
        })
    }
}