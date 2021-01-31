@file:JvmName("SystemMediaSelectorCreator")

package com.android.sdk.mediaselector.system

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.*
import timber.log.Timber

/**
 * 通过系统相机或者系统 SAF 获取照片、文件。
 *
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-06 18:03
 */
interface SystemMediaSelector : ActivityStateHandler {

    fun takePhotoFromCamera(): Instructor

    fun takePhotoFromSystem(): Instructor

    fun takeFileFromSystem(): Instructor

}

fun newSystemMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val legacySystemMediaSelector = LegacySystemMediaSelector(activity, resultListener)
        autoCallback(activity, legacySystemMediaSelector)
        legacySystemMediaSelector
    } else {
        Timber.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val androidPSystemMediaSelector = AndroidQSystemMediaSelector(activity, resultListener)
        autoCallback(activity, androidPSystemMediaSelector)
        androidPSystemMediaSelector
    }
}

fun newSystemMediaSelector(fragment: Fragment, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val legacySystemMediaSelector = LegacySystemMediaSelector(fragment, resultListener)
        autoCallback(fragment, legacySystemMediaSelector)
        legacySystemMediaSelector
    } else {
        Timber.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        val androidPSystemMediaSelector = AndroidQSystemMediaSelector(fragment, resultListener)
        autoCallback(fragment, androidPSystemMediaSelector)
        androidPSystemMediaSelector
    }
}