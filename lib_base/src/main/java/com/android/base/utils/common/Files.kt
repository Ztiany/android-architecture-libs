@file:JvmName("Files")

package com.android.base.utils.common

import com.blankj.utilcode.util.FileUtils
import timber.log.Timber
import java.io.File

fun File?.makeFilePath(): Boolean {
    if (this == null) {
        return false
    }
    val parent = this.parentFile
    return parent.exists() || makeDir(parent)
}

private fun makeDir(file: File?): Boolean {
    return file != null && (file.exists() || file.mkdirs())
}

fun File?.sizeOf(): Long {
    if (this == null || !this.exists()) {
        return 0
    }
    return if (this.isDirectory) {
        var length: Long = 0
        for (subFile in this.listFiles()) {
            length += subFile.sizeOf()
        }
        length
    } else {
        this.length()
    }
}

fun File.deleteSelf() {
    if (this.isDirectory) {
        for (subFile in this.listFiles()) {
            subFile.deleteSelf()
        }
    } else {
        val delete = this.delete()
        Timber.d("deleteFile: $delete")
    }
}

fun File?.isFileExists(): Boolean {
    return this != null && this.isFile && this.exists()
}

/**
 * 获取全路径中的文件拓展名
 *
 * @return 文件拓展名
 */
fun File?.getFileExtension(): String? {
    return if (this == null) null else getFileExtension(this.path)
}

/**
 * 获取全路径中的文件拓展名
 *
 * @param filePath 文件路径
 * @return 文件拓展名
 */
fun getFileExtension(filePath: String): String {
    return FileUtils.getFileExtension(filePath)
}