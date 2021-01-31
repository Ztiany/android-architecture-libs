package com.android.sdk.mediaselector.system

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.sdk.mediaselector.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-06 18:07
 *
 *  refer:
 *
 * - [action-open-document-with-storage-access-framework-returns-duplicate-results](https://stackoverflow.com/questions/39804530/action-open-document-with-storage-access-framework-returns-duplicate-results)
 * - [document-provider](https://developer.android.com/guide/topics/providers/document-provider?hl=zh-cn)
 */
@RequiresApi(Build.VERSION_CODES.O)
internal class AndroidQSystemMediaSelector : BaseSystemMediaSelector {

    constructor(activity: AppCompatActivity, resultListener: ResultListener) : super(activity, resultListener)

    constructor(fragment: Fragment, resultListener: ResultListener) : super(fragment, resultListener)

    override fun doTakePhotoFormSystem(): Boolean {
        return openASF(REQUEST_ALBUM, MediaUtils.MIMETYPE_IMAGE)
    }

    override fun processSystemPhotoResult(data: Intent?) {
        if (data == null) {
            mediaSelectorCallback.onTakeFail()
            return
        }

        val uri = data.data
        val clipData = data.clipData

        when {
            clipData != null -> returnMultiData(clipData)
            uri != null -> handleSinglePhoto(uri)
            else -> mediaSelectorCallback.onTakeFail()
        }
    }

    private fun handleSinglePhoto(uri: Uri) {
        if (currentInstructor.isCopyToInternal || currentInstructor.needCrop()) {

            lifecycleOwner.lifecycleScope.launch {

                val copied = withContext(Dispatchers.IO) {
                    copySingleToInternal(context, uri)
                }

                when {
                    copied == null -> mediaSelectorCallback.onTakeFail()
                    currentInstructor.needCrop() -> toCrop(copied)
                    else -> mediaSelectorCallback.onTakeSuccess(newUriList(copied))
                }
            }

            return
        }

        mediaSelectorCallback.onTakeSuccess(listOf(uri))
    }

    override fun doTakeFile(): Boolean {
        return openASF(REQUEST_FILE, MediaUtils.MIMETYPE_ALL)
    }

    override fun processFileResult(data: Intent?) {
        if (data == null) {
            mediaSelectorCallback.onTakeFail()
            return
        }
        val uri = data.data
        val clipData = data.clipData
        when {
            clipData != null -> returnMultiData(clipData)
            uri != null -> mediaSelectorCallback.onTakeSuccess(listOf(uri))
            else -> mediaSelectorCallback.onTakeFail()
        }
    }

    private fun openASF(requestCode: Int, defaultType: String): Boolean {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, currentInstructor.isMultiple)
        intent.type = if (currentInstructor.mimeType.isNullOrBlank()) defaultType else currentInstructor.mimeType

        return try {
            startActivityForResult(intent, requestCode)
            true
        } catch (e: Exception) {
            Timber.d(e, "doTakePhotoFormSystem")
            false
        }
    }

    private fun returnMultiData(clipData: ClipData) {
        if (currentInstructor.isCopyToInternal) {
            copyToInternalAndReturn(clipData)
        } else {
            val uris: MutableList<Uri> = ArrayList()
            for (i in 0 until clipData.itemCount) {
                uris.add(clipData.getItemAt(i).uri)
            }
            mediaSelectorCallback.onTakeSuccess(uris)
        }
    }

    private fun copyToInternalAndReturn(clipData: ClipData) {
        lifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val list = mutableListOf<String>()
                for (i in 0 until clipData.itemCount) {
                    copySingleToInternal(context, clipData.getItemAt(i).uri)?.let(list::add)
                }
                list
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