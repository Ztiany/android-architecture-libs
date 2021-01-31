@file:JvmName("Files")

package com.android.base.utils.common

import com.blankj.utilcode.util.FileUtils
import timber.log.Timber
import java.io.File

fun File.makeParentPath(): Boolean {
    val path = this.parentFile ?: return false
    return path.exists() || makeDir(path)
}

private fun makeDir(file: File?): Boolean {
    return file != null && (file.exists() || file.mkdirs())
}

fun File.sizeOf(): Long {
    if (!this.exists()) {
        return 0
    }
    return if (this.isDirectory) {
        var length: Long = 0
        val listFiles = this.listFiles() ?: return 0
        for (subFile in listFiles) {
            length += subFile.sizeOf()
        }
        length
    } else {
        this.length()
    }
}

fun File.deleteSelf() {
    if (this.isDirectory) {
        val listFiles = this.listFiles() ?: return
        for (subFile in listFiles) {
            subFile.deleteSelf()
        }
    } else {
        val delete = this.delete()
        Timber.d("deleteFile: $delete")
    }
}

fun File.isFileExists(): Boolean {
    return this.isFile && this.exists()
}

/**
 * 获取全路径中的文件拓展名
 *
 * @return 文件拓展名
 */
fun File.getFileExtension(): String {
    return FileUtils.getFileExtension(this.absolutePath)
}