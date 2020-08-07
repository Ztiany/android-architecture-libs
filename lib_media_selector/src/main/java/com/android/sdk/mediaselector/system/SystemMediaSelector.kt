@file:JvmName("SystemMediaSelectorCreator")

package com.android.sdk.mediaselector.system

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.LogUtils
import com.android.sdk.mediaselector.common.ResultListener

/**
 * 通过系统相机或者系统 SAF 获取照片、文件。
 *
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-06 18:03
 */
interface SystemMediaSelector {

    fun takePhotoFromCamera(): Instructor

    fun takePhotoFromSystem(): Instructor

    fun takeFileFromSystem(): Instructor

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onSaveInstanceState(outState: Bundle)

    fun onRestoreInstanceState(outState: Bundle?)

}

fun newSystemMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        val legacySystemMediaSelector = LegacySystemMediaSelector(activity, resultListener)
        AutoRestoreDelegates.autoCallback(activity, legacySystemMediaSelector)
        legacySystemMediaSelector
    } else {
        LogUtils.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val androidPSystemMediaSelector = AndroidPSystemMediaSelector(activity, resultListener)
        AutoRestoreDelegates.autoCallback(activity, androidPSystemMediaSelector)
        androidPSystemMediaSelector
    }
}

fun newSystemMediaSelector(fragment: Fragment, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        val legacySystemMediaSelector = LegacySystemMediaSelector(fragment, resultListener)
        AutoRestoreDelegates.autoCallback(fragment, legacySystemMediaSelector)
        legacySystemMediaSelector
    } else {
        LogUtils.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        val androidPSystemMediaSelector = AndroidPSystemMediaSelector(fragment, resultListener)
        AutoRestoreDelegates.autoCallback(fragment, androidPSystemMediaSelector)
        androidPSystemMediaSelector
    }
}