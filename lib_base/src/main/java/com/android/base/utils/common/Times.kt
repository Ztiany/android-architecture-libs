package com.android.base.utils.common

import java.text.SimpleDateFormat
import java.util.*

fun formatMilliseconds(milliseconds: Long, pattern: String = "yyyy-MM-dd"): String {
    val sdp = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date()
    date.time = milliseconds
    return sdp.format(date)
}
