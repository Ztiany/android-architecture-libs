@file:JvmName("Sizes")

package me.ztiany.widget.common

import android.content.Context
import android.util.TypedValue
import android.view.View

fun View.dip(value: Int): Int = dpToPx(context, value)
fun View.dip(value: Float): Float = dpToPx(context, value)
fun View.sp(value: Int): Int = spToPx(context, value)
fun View.sp(value: Float): Float = spToPx(context, value)

fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
}

fun pxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun pxToDp(context: Context, px: Int): Int {
    return (px / context.resources.displayMetrics.density + 0.5f).toInt()
}

fun spToPx(context: Context, sp: Float): Float {
    return sp * context.resources.displayMetrics.scaledDensity
}

fun spToPx(context: Context, sp: Int): Int {
    return (sp * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun pxToSp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.scaledDensity
}

fun pxToSp(context: Context, px: Int): Int {
    return (px / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

/**
 * 各种单位转换，该方法存在于[TypedValue] 中
 *
 * @param unit  单位
 * @param value 值
 * @return 转换结果
 */
fun applyDimension(context: Context, unit: Int, value: Float): Float {
    val metrics = context.resources.displayMetrics
    when (unit) {
        TypedValue.COMPLEX_UNIT_PX -> return value
        TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
        TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
        TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0F / 72)
        TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
        TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0F / 25.4F)
    }
    return 0f
}
