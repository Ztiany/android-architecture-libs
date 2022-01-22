package com.android.base.utils.android.views

import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView


fun ScrollView.scrollToBottom() {
    fullScroll(View.FOCUS_DOWN)
}

fun ScrollView.scrollToTop() {
    fullScroll(View.FOCUS_UP)
}

fun NestedScrollView.scrollToBottom() {
    fullScroll(View.FOCUS_DOWN)
}

fun NestedScrollView.scrollToTop() {
    fullScroll(View.FOCUS_UP)
}
