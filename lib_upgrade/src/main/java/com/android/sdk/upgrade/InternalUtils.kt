@file:JvmName("InternalUtils")

package com.android.sdk.upgrade

import java.io.Closeable
import java.io.File

internal fun File.makeParentPath(): Boolean {
    return makeDir(this.parentFile)
}

private fun makeDir(file: File?): Boolean {
    return file != null && (file.exists() || file.mkdirs())
}

internal fun closeIOQuietly(vararg closable: Closeable?) {
    closable.forEach {
        if (it != null) {
            try {
                it.close()
            } catch (ignore: Exception) {
            }
        }
    }
}

internal fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}