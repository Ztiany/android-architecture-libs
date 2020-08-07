package com.android.sdk.mediaselector.system

import android.net.Uri
import java.io.File
import java.util.*

fun newUriList(filePath: String): List<Uri> {
    val result: MutableList<Uri> = ArrayList()
    result.add(Uri.fromFile(File(filePath)))
    return result
}