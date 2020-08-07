package com.android.sdk.system

import android.net.Uri
import java.io.File
import java.util.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-07 21:06
 */
///////////////////////////////////////////////////////////////////////////
// Tools
///////////////////////////////////////////////////////////////////////////
fun newUriList(filePath: String): List<Uri> {
    val result: MutableList<Uri> = ArrayList()
    result.add(Uri.fromFile(File(filePath)))
    return result
}