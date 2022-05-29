package com.android.base.architecture.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.base.foundation.activity.ActivityDelegate
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.foundation.activity.ActivityState
import com.android.base.utils.android.compat.AndroidVersion
import timber.log.Timber

/**
 * 基础 BaseActivity 封装：
 *
 * 1. 封装通用流程。
 * 2. [onBackPressed] 事件分发，优先交给 [Fragment] 处理。
 *
 * @author Ztiany
 * Date : 2016-05-04 15:40
 */
abstract class BaseActivity : AppCompatActivity(), ActivityDelegateOwner {

    private val activityDelegates by lazy { ActivityDelegates(this) }

    private var activityState = ActivityState.INITIALIZED

    private fun tag() = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(tag()).d("---->onCreate before call super")
        initialize(savedInstanceState)
        activityDelegates.callOnCreateBeforeSetContentView(savedInstanceState)
        super.onCreate(savedInstanceState)
        Timber.tag(tag()).d("---->onCreate after call super  bundle = $savedInstanceState")

        when (val layout = provideLayout()) {
            is View -> setContentView(layout)
            is Int -> setContentView(layout)
            null -> Timber.d("layout() return null layout")
            else -> throw IllegalArgumentException("layout() return type no support, layout = $layout")
        }

        activityState = ActivityState.CREATE
        activityDelegates.callOnCreateAfterSetContentView(savedInstanceState)
        setUpLayout(savedInstanceState)
    }

    override fun onRestart() {
        Timber.tag(tag()).d("---->onRestart before call super")
        super.onRestart()
        Timber.tag(tag()).d("---->onRestart after call super  ")
        activityDelegates.callOnRestart()
    }

    override fun onStart() {
        Timber.tag(tag()).d("---->onStart before call super")
        super.onStart()
        Timber.tag(tag()).d("---->onStart after call super")
        activityState = ActivityState.START
        activityDelegates.callOnStart()
    }

    override fun onResume() {
        Timber.tag(tag()).d("---->onResume before call super")
        super.onResume()
        Timber.tag(tag()).d("---->onResume after call super")
        activityState = ActivityState.RESUME
        activityDelegates.callOnResume()
    }

    override fun onPause() {
        Timber.tag(tag()).d("---->onPause before call super")
        activityState = ActivityState.PAUSE
        activityDelegates.callOnPause()
        super.onPause()
        Timber.tag(tag()).d("---->onPause after call super  ")
    }

    override fun onStop() {
        Timber.tag(tag()).d("---->onStop before call super")
        activityState = ActivityState.STOP
        activityDelegates.callOnStop()
        super.onStop()
        Timber.tag(tag()).d("---->onStop after call super")
    }

    override fun onDestroy() {
        Timber.tag(tag()).d("---->onDestroy before call super")
        activityState = ActivityState.DESTROY
        activityDelegates.callOnDestroy()
        super.onDestroy()
        Timber.tag(tag()).d("---->onDestroy after call super")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        activityDelegates.callOnPostCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityDelegates.callOnSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        activityDelegates.callOnRestoreInstanceState(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityDelegates.callOnActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activityDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        activityDelegates.callOnResumeFragments()
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface impl
    ///////////////////////////////////////////////////////////////////////////
    @UiThread
    final override fun addDelegate(activityDelegate: ActivityDelegate<*>) {
        activityDelegates.addActivityDelegate(activityDelegate)
    }

    @UiThread
    final override fun removeDelegate(activityDelegate: ActivityDelegate<*>): Boolean {
        return activityDelegates.removeActivityDelegate(activityDelegate)
    }

    @UiThread
    final override fun findDelegate(predicate: (ActivityDelegate<*>) -> Boolean): ActivityDelegate<*>? {
        return activityDelegates.findDelegate(predicate)
    }

    override fun getStatus(): ActivityState {
        return activityState
    }

    /**
     * Before calling super.onCreate and setContentView
     *
     * @param savedInstanceState state
     */
    protected open fun initialize(savedInstanceState: Bundle?) {}

    /**
     * provide a layoutId (int) or layout (View)
     *
     * @return layoutId
     */
    protected open fun provideLayout(): Any? = null

    /**
     * after calling setContentView
     */
    protected abstract fun setUpLayout(savedInstanceState: Bundle?)

    override fun onBackPressed() {
        if (BackHandlerHelper.handleBackPress(this)) {
            Timber.d("onBackPressed() called but child fragment handle it")
        } else {
            superOnBackPressed()
        }
    }

    protected open fun superOnBackPressed() {
        super.onBackPressed()
    }

    override fun isDestroyed(): Boolean {
        return if (AndroidVersion.atLeast(17)) {
            super.isDestroyed()
        } else {
            activityState === ActivityState.DESTROY
        }
    }

}