package com.android.base.utils.common

import java.text.SimpleDateFormat
import java.util.*

const val TIME_PATTERN_LONG = "yyyy-MM-dd HH:mm:ss"

fun formatMilliseconds(milliseconds: Long, pattern: String = "yyyy-MM-dd"): String {
    val sdp = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date()
    date.time = milliseconds
    return sdp.format(date)
}
