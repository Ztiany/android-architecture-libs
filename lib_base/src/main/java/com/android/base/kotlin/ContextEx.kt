package com.android.base.kotlin

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

fun Context.colorFromId(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.drawableFromId(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}