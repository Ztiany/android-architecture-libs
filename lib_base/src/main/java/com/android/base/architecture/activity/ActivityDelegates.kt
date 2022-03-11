package com.android.base.architecture.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.android.base.foundation.activity.ActivityDelegate

/**
 * @author Ztiany
 * Date : 2016-12-20 11:43
 */
@UiThread
internal class ActivityDelegates(
    private val baseActivity: AppCompatActivity
) {

    private val delegates: MutableList<ActivityDelegate<*>> = ArrayList(4)

    fun callOnCreateBeforeSetContentView(savedInstanceState: Bundle?) {
        for (activityDelegate in delegates) {
            activityDelegate.onCreateBeforeSetContentView(savedInstanceState)
        }
    }

    fun callOnCreateAfterSetContentView(savedInstanceState: Bundle?) {
        for (activityDelegate in delegates) {
            activityDelegate.onCreateAfterSetContentView(savedInstanceState)
        }
    }

    fun callOnRestoreInstanceState(savedInstanceState: Bundle?) {
        for (activityDelegate in delegates) {
            activityDelegate.onRestoreInstanceState(savedInstanceState!!)
        }
    }

    fun callOnPostCreate(savedInstanceState: Bundle?) {
        for (activityDelegate in delegates) {
            activityDelegate.onPostCreate(savedInstanceState)
        }
    }

    fun callOnSaveInstanceState(outState: Bundle) {
        for (activityDelegate in delegates) {
            activityDelegate.onSaveInstanceState(outState)
        }
    }

    fun callOnDestroy() {
        for (activityDelegate in delegates) {
            activityDelegate.onDestroy()
        }
    }

    fun callOnStop() {
        for (activityDelegate in delegates) {
            activityDelegate.onStop()
        }
    }

    fun callOnPause() {
        for (activityDelegate in delegates) {
            activityDelegate.onPause()
        }
    }

    fun callOnResume() {
        for (activityDelegate in delegates) {
            activityDelegate.onResume()
        }
    }

    fun callOnStart() {
        for (activityDelegate in delegates) {
            activityDelegate.onStart()
        }
    }

    fun callOnRestart() {
        for (activityDelegate in delegates) {
            activityDelegate.onRestart()
        }
    }

    fun callOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (activityDelegate in delegates) {
            activityDelegate.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun callOnRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (activityDelegate in delegates) {
            activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun callOnResumeFragments() {
        for (activityDelegate in delegates) {
            activityDelegate.onResumeFragments()
        }
    }

    fun addActivityDelegate(activityDelegate: ActivityDelegate<*>) {
        delegates.add(activityDelegate)
        @Suppress("UNCHECKED_CAST")
        (activityDelegate as ActivityDelegate<Activity>).onAttachedToActivity(baseActivity)
    }

    fun removeActivityDelegate(activityDelegate: ActivityDelegate<*>): Boolean {
        val remove = delegates.remove(activityDelegate)
        if (remove) {
            activityDelegate.onDetachedFromActivity()
        }
        return remove
    }

    fun findDelegate(predicate: (ActivityDelegate<*>) -> Boolean): ActivityDelegate<*>? {
        for (delegate in delegates) {
            if (predicate(delegate)) {
                return delegate
            }
        }
        return null
    }

}