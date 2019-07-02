package com.android.base.kotlin

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.base.utils.android.UnitConverter

fun Context.dip(value: Int): Int = UnitConverter.dpToPx(value)
fun Context.dip(value: Float): Float = UnitConverter.dpToPx(value)
fun Context.sp(value: Int): Int = UnitConverter.spToPx(value)
fun Context.sp(value: Float): Float = UnitConverter.spToPx(value)

fun Fragment.dip(value: Int): Int = UnitConverter.dpToPx(value)
fun Fragment.dip(value: Float): Float = UnitConverter.dpToPx(value)
fun Fragment.sp(value: Int): Int = UnitConverter.spToPx(value)
fun Fragment.sp(value: Float): Float = UnitConverter.spToPx(value)

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Float = context.dip(value)
fun View.sp(value: Int): Int = context.sp(value)
fun View.sp(value: Float): Float = context.sp(value)

fun RecyclerView.ViewHolder.dip(value: Int): Int = itemView.dip(value)
fun RecyclerView.ViewHolder.dip(value: Float): Float = itemView.dip(value)
fun RecyclerView.ViewHolder.sp(value: Int): Int = itemView.dip(value)
fun RecyclerView.ViewHolder.sp(value: Float): Float = itemView.dip(value)