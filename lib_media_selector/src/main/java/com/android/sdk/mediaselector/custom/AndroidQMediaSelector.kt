package com.android.sdk.mediaselector.custom

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.sdk.mediaselector.common.*
import com.android.sdk.mediaselector.common.copySingleToInternal
import com.bilibili.boxing.model.entity.BaseMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.ArrayList

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 10:49
 */
@RequiresApi(Build.VERSION_CODES.O)
internal class AndroidQMediaSelector : BaseMediaSelector {

    constructor(activity: AppCompatActivity, resultListener: ResultListener) : super(activity, resultListener)

    constructor(fragment: Fragment, resultListener: ResultListener) : super(fragment, resultListener)

    override fun handleSingleResult(baseMedia: BaseMedia) {
        Timber.d("handleMultiResult() called with: medias = $baseMedia")

        if (currentInstructor.isCopyToInternal || currentInstructor.isNeedCrop) {

            val absolutePath = MediaUtils.getAbsolutePath(context, baseMedia.uri) ?: ""
            val alreadyInternal = absolutePath.contains("Android/data/${context.packageName}")

            Timber.d("handleSingleResult alreadyInternal = $alreadyInternal")

            if (alreadyInternal) {
                when {
                    currentInstructor.isNeedCrop -> toCrop(absolutePath)
                    else -> mediaSelectorCallback.onTakeSuccess(newUriList(absolutePath))
                }
            } else {
                copyAndHandleSingleResult(baseMedia)
            }

            return
        }
        mediaSelectorCallback.onTakeSuccess(listOf(baseMedia.uri))
    }

    private fun copyAndHandleSingleResult(baseMedia: BaseMedia) {
        lifecycleOwner.lifecycleScope.launch {

            val copied = withContext(Dispatchers.IO) {
                copySingleToInternal(context, baseMedia.uri)
            }

            when {
                copied == null -> mediaSelectorCallback.onTakeFail()
                currentInstructor.isNeedCrop -> toCrop(copied)
                else -> mediaSelectorCallback.onTakeSuccess(newUriList(copied))
            }
        }
    }

    override fun handleMultiResult(medias: ArrayList<BaseMedia>) {
        Timber.d("handleMultiResult() called with: medias = $medias")
        if (currentInstructor.isCopyToInternal) {
            copyToInternalAndReturn(medias)
        } else {
            medias.map {
                it.uri
            }.let(mediaSelectorCallback::onTakeSuccess)
        }
    }

    private fun copyToInternalAndReturn(medias: ArrayList<BaseMedia>) {
        lifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                medias.mapNotNull {
                    copySingleToInternal(context, it.uri)
                }
            }.map {
                Uri.fromFile(File(it))
            }.let {
                if (it.isEmpty()) {
                    mediaSelectorCallback.onTakeFail()
                } else {
                    mediaSelectorCallback.onTakeSuccess(it)
                }
            }
        }
    }

}