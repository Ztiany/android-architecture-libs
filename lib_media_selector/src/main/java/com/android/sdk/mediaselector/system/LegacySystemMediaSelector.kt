package com.android.sdk.mediaselector.system

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.common.MediaUtils
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.common.newUriList
import timber.log.Timber
import java.io.File
import java.util.*

internal class LegacySystemMediaSelector : BaseSystemMediaSelector {

    constructor(activity: AppCompatActivity, resultListener: ResultListener) : super(activity, resultListener)

    constructor(fragment: Fragment, resultListener: ResultListener) : super(fragment, resultListener)

    ///////////////////////////////////////////////////////////////////////////
    // Album
    ///////////////////////////////////////////////////////////////////////////

    override fun doTakePhotoFormSystem(): Boolean {
        return openContentSelector(REQUEST_ALBUM, MediaUtils.MIMETYPE_IMAGE)
    }

    override fun processSystemPhotoResult(data: Intent?) {
        if (data == null) {
            mediaSelectorCallback.onTakeFail()
            return
        }
        val clipData = data.clipData
        if (clipData != null) {
            returnMultiDataChecked(clipData)
            return
        }

        val uri = data.data
        if (uri == null) {
            mediaSelectorCallback.onTakeFail()
            return
        }

        if (currentInstructor.needCrop()) {
            val absolutePath = MediaUtils.getAbsolutePath(context, uri)
            if (absolutePath.isNullOrBlank()) {
                mediaSelectorCallback.onTakeFail()
            } else {
                toCrop(absolutePath)
            }
        } else {
            returnSingleDataChecked(uri)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // File
    ///////////////////////////////////////////////////////////////////////////

    override fun doTakeFile(): Boolean {
        return openContentSelector(REQUEST_FILE, MediaUtils.MIMETYPE_ALL)
    }

    override fun processFileResult(data: Intent?) {
        if (data == null) {
            mediaSelectorCallback.onTakeFail()
            return
        }

        val clipData = data.clipData
        if (clipData != null) {
            returnMultiDataChecked(clipData)
            return
        }

        val uri = data.data
        if (uri == null) {
            mediaSelectorCallback.onTakeFail()
        } else {
            returnSingleDataChecked(uri)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utils
    ///////////////////////////////////////////////////////////////////////////

    private fun openContentSelector(requestCode: Int, defaultType: String): Boolean {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, currentInstructor.isMultiple)
        intent.type = if (currentInstructor.mimeType.isNullOrBlank()) defaultType else currentInstructor.mimeType

        return try {
            startActivityForResult(intent, requestCode)
            true
        } catch (e: Exception) {
            Timber.e(e, "doTakePhotoFormSystem")
            false
        }
    }

    private fun returnMultiDataChecked(clipData: ClipData) {
        val uris: MutableList<Uri> = ArrayList()

        for (i in 0 until clipData.itemCount) {
            val absolutePath = MediaUtils.getAbsolutePath(context, clipData.getItemAt(i).uri)
            if (!absolutePath.isNullOrBlank()) {
                val file = File(absolutePath)
                uris.add(Uri.fromFile(file))
            }
        }

        if (uris.isEmpty()) {
            mediaSelectorCallback.onTakeFail()
        } else {
            mediaSelectorCallback.onTakeSuccess(uris)
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