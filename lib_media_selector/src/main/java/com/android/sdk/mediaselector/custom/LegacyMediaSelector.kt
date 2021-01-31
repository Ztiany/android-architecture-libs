package com.android.sdk.mediaselector.custom

import android.net.Uri
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.MediaUtils
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.common.newUriList
import com.bilibili.boxing.model.entity.BaseMedia
import java.io.File
import java.util.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 10:49
 */
internal class LegacyMediaSelector : BaseMediaSelector {

    constructor(activity: AppCompatActivity, resultListener: ResultListener) : super(activity, resultListener)

    constructor(fragment: Fragment, resultListener: ResultListener) : super(fragment, resultListener)

    override fun handleSingleResult(baseMedia: BaseMedia) {
        val cropOptions = currentInstructor.cropOptions
        if (cropOptions == null) {
            returnSingleDataChecked(baseMedia.uri)
        } else {
            val absolutePath = MediaUtils.getAbsolutePath(context, baseMedia.uri)
            if (absolutePath.isNullOrBlank()) {
                mediaSelectorCallback.onTakeFail()
            } else {
                toCrop(absolutePath)
            }
        }
    }

    override fun handleMultiResult(medias: ArrayList<BaseMedia>) {
        val result = medias.mapNotNull {
            MediaUtils.getAbsolutePath(context, it.uri)?.run {
                Uri.fromFile(File(this))
            }
        }

        if (result.isEmpty()) {
            mediaSelectorCallback.onTakeFail()
        } else {
            mediaSelectorCallback.onTakeSuccess(result)
        }
    }

    private fun returnSingleDataChecked(uri: Uri) {
        val absolutePath = MediaUtils.getAbsolutePath(context, uri)
        if (TextUtils.isEmpty(absolutePath)) {
            mediaSelectorCallback.onTakeFail()
        } else {
            mediaSelectorCallback.onTakeSuccess(newUriList(absolutePath))
        }
    }

}