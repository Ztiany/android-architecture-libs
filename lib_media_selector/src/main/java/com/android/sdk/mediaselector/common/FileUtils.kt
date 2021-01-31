package com.android.sdk.mediaselector.common

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 11:04
 */
@RequiresApi(Build.VERSION_CODES.O)
internal fun copySingleToInternal(context: Context, uri: Uri): String? {
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null, null)
    var postfix = cursor?.use {
        if (it.moveToFirst()) {
            StorageUtils.getFileExtension(it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME)))
        } else ""
    }

    // TODO  get file extension via reading binary.
    if (postfix.isNullOrBlank()) {
        postfix = StorageUtils.JPEG
    }

    return try {
        val target = StorageUtils.createInternalPicturePath(context, postfix)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            Files.copy(inputStream, Paths.get(target))
        }
        target
    } catch (e: Exception) {
        Timber.d("copySingleToInternal() $e")
        null
    }
}

fun newUriList(filePath: String): List<Uri> {
    val result: MutableList<Uri> = ArrayList()
    result.add(Uri.fromFile(File(filePath)))
    return result
}