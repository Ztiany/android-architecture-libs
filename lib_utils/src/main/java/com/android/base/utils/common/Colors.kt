package com.android.base.utils.common


/** see [how-to-convert-a-color-integer-to-a-hex-string-in-android](https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android)*/
fun toHexColor(color: Int) = String.format("#%06X", 0xFFFFFF and color)
