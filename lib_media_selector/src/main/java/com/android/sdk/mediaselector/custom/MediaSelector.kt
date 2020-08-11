package com.android.sdk.mediaselector.custom

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.ActivityStateHandler
import com.android.sdk.mediaselector.common.LogUtils
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.common.AutoRestoreDelegates

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 10:07
 */
interface MediaSelector : ActivityStateHandler {

    fun takeMedia(): Instructor

}

fun newMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(activity, resultListener)
        AutoRestoreDelegates.autoCallback(activity, mediaSelector)
        mediaSelector
    } else {
        LogUtils.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(activity, resultListener)
        AutoRestoreDelegates.autoCallback(activity, mediaSelector)
        mediaSelector
    }
}

fun newMediaSelector(fragment: Fragment, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29) {
        LogUtils.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(fragment, resultListener)
        AutoRestoreDelegates.autoCallback(fragment, mediaSelector)
        mediaSelector
    } else {
        LogUtils.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(fragment, resultListener)
        AutoRestoreDelegates.autoCallback(fragment, mediaSelector)
        mediaSelector
    }
}
