package com.android.sdk.mediaselector.system

import android.content.ClipData
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.sdk.mediaselector.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
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
internal class AndroidPSystemMediaSelector : BaseSystemMediaSelector {

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
                    copySingleToInternal(uri)
                }
                if (currentInstructor.needCrop()) {
                    toCrop(copied)
                } else {
                    mediaSelectorCallback.onTakeSuccess(newUriList(copied))
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
            LogUtils.e("doTakePhotoFormSystem", e)
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
                    list.add(copySingleToInternal(clipData.getItemAt(i).uri))
                }
                list
            }.map {
                Uri.fromFile(File(it))
            }.let(mediaSelectorCallback::onTakeSuccess)
        }
    }

    private fun copySingleToInternal(uri: Uri): String {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null, null)
        var postfix = cursor?.use {
            if (it.moveToFirst()) {
                StorageUtils.getFileExtension(it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME)))
            } else ""
        }

        // TODO  get file extension by reading binary.
        if (postfix.isNullOrBlank()) {
            postfix = StorageUtils.JPEG
        }

        val target = StorageUtils.createInternalPicturePath(context, postfix)

        LogUtils.d(target)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            Files.copy(inputStream, Paths.get(target))
        }

        return target
    }

}