package com.android.sdk.mediaselector.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 裁剪配置类
 * Author: JPH
 * Date: 2016/7/27 13:19
 */
@Parcelize
data class CropOptions(
        /**裁剪宽度比例 与aspectY组合，如16:9*/
        val aspectX: Int = 0,
        /** 高度比例与aspectX组合，如16:9*/
        val aspectY: Int = 0,
        /**输出图片的宽度*/
        val outputX: Int = 0,
        /**输入图片的高度*/
        val outputY: Int = 0
) : Parcelable