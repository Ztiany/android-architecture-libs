package com.android.sdk.mediaselector.custom

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.*
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 10:07
 */
interface MediaSelector : ActivityStateHandler {

    fun takeMedia(): Instructor

}

fun newMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(activity, resultListener)
        autoCallback(activity, mediaSelector)
        mediaSelector
    } else {
        Timber.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(activity, resultListener)
        autoCallback(activity, mediaSelector)
        mediaSelector
    }
}

fun newMediaSelector(fragment: Fragment, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(fragment, resultListener)
        autoCallback(fragment, mediaSelector)
        mediaSelector
    } else {
        Timber.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(fragment, resultListener)
        autoCallback(fragment, mediaSelector)
        mediaSelector
    }
}
