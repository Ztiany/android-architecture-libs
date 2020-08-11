@file:JvmName("SystemMediaSelectorCreator")

package com.android.sdk.mediaselector.system

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.ActivityStateHandler
import com.android.sdk.mediaselector.common.AutoRestoreDelegates
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
interface SystemMediaSelector: ActivityStateHandler {

    fun takePhotoFromCamera(): Instructor

    fun takePhotoFromSystem(): Instructor

    fun takeFileFromSystem(): Instructor

}

fun newSystemMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        val legacySystemMediaSelector = LegacySystemMediaSelector(activity, resultListener)
        AutoRestoreDelegates.autoCallback(activity, legacySystemMediaSelector)
        legacySystemMediaSelector
    } else {
        LogUtils.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val androidPSystemMediaSelector = AndroidQSystemMediaSelector(activity, resultListener)
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
        val androidPSystemMediaSelector = AndroidQSystemMediaSelector(fragment, resultListener)
        AutoRestoreDelegates.autoCallback(fragment, androidPSystemMediaSelector)
        androidPSystemMediaSelector
    }
}