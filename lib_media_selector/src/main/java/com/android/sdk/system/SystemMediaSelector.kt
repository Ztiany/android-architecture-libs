@file:JvmName("SystemMediaSelectorCreator")

package com.android.sdk.system

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.common.LogUtils
import com.android.sdk.common.ResultListener

/**
 * 通过系统相册或者系统 SAF 获取照片、文件。
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
        LegacySystemMediaSelector(activity, resultListener)
    } else {
        LogUtils.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        AndroidPSystemMediaSelector(activity, resultListener)
    }
}

fun newSystemMediaSelector(fragment: Fragment, resultListener: ResultListener): SystemMediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        LegacySystemMediaSelector(fragment, resultListener)
    } else {
        LogUtils.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        AndroidPSystemMediaSelector(fragment, resultListener)
    }
}