package com.bilibili.boxing.model.callback

import android.net.Uri
import android.os.Parcelable

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-10-14 15:09
 */
interface MediaFilter : Parcelable {

    /**Return true means the Media will be filtered.*/
    fun filterPath(uri: Uri): Boolean

    /**Return true means the Media will be filtered.*/
    fun filerSize(size: Long): Boolean

}