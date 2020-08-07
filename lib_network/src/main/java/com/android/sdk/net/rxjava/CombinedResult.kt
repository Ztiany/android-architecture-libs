package com.android.sdk.net.rxjava

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-10 11:11
 */
data class CombinedResult<T>(
        val dataType: DataType,
        val data: T?,
        val error: Throwable?
)

enum class DataType {
    Remote, Disk
}