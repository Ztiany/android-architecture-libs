package com.android.sdk.mediaselector.common

import android.os.Parcel
import android.os.Parcelable

/**
 * 裁剪配置类
 */
data class CropOptions(
        /**裁剪宽度比例 与aspectY组合，如16:9*/
        val aspectX: Int = 0,
        /** 高度比例与aspectX组合，如16:9*/
        val aspectY: Int = 0,
        /**输出图片的宽度*/
        val outputX: Int = 0,
        /**输入图片的高度*/
        val outputY: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(aspectX)
        parcel.writeInt(aspectY)
        parcel.writeInt(outputX)
        parcel.writeInt(outputY)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CropOptions> {
        override fun createFromParcel(parcel: Parcel): CropOptions {
            return CropOptions(parcel)
        }

        override fun newArray(size: Int): Array<CropOptions?> {
            return arrayOfNulls(size)
        }
    }

}