@file:JvmName("Bitmaps")

package com.android.base.utils.android

import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.android.base.utils.common.makeParentPath
import java.io.File
import java.io.FileOutputStream


fun Bitmap.saveToFile(format: Bitmap.CompressFormat, @IntRange(from = 1, to = 100) quality: Int, savePath: String): Boolean {
    return try {
        val file = File(savePath)
        file.makeParentPath()
        file.createNewFile()
        compress(format, quality, FileOutputStream(file))
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}