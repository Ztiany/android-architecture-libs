package com.android.base.foundation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Activity生命周期代理
 *
 * @author Ztiany
 * Date : 2016-05-06 15:04
 */
interface ActivityDelegate<T : Activity> {

    fun onAttachedToActivity(activity: T) {}
    fun onDetachedFromActivity() {}

    fun onCreateBeforeSetContentView(savedInstanceState: Bundle?) {}
    fun onCreateAfterSetContentView(savedInstanceState: Bundle?) {}
    fun onStart() {}
    fun onRestart() {}
    fun onResume() {}
    fun onPause() {}
    fun onStop() {}
    fun onDestroy() {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    fun onSaveInstanceState(savedInstanceState: Bundle) {}
    fun onRestoreInstanceState(savedInstanceState: Bundle) {}

    fun onPostCreate(savedInstanceState: Bundle?) {}
    fun onResumeFragments() {}

}