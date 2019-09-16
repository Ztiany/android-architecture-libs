package com.android.base.kotlin

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.colorFromId(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.drawableFromId(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}