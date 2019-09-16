package com.android.base.kotlin

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.colorFromId(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun Fragment.drawableFromId(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}
