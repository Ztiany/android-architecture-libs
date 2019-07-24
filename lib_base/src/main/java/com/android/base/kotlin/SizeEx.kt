package com.android.base.kotlin

import com.android.base.utils.android.UnitConverter

fun dip(value: Int): Int = UnitConverter.dpToPx(value)
fun dip(value: Float): Float = UnitConverter.dpToPx(value)
fun sp(value: Int): Int = UnitConverter.spToPx(value)
fun sp(value: Float): Float = UnitConverter.spToPx(value)