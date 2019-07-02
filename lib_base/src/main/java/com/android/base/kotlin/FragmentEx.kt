package com.android.base.kotlin

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat


fun Fragment.colorFromId(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun Fragment.drawableFromId(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}
