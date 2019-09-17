package com.android.base.utils.android.views

import android.util.TypedValue
import com.android.base.utils.BaseUtils

fun dip(value: Int): Int = dpToPx(value)
fun dip(value: Float): Float = dpToPx(value)
fun sp(value: Int): Int = spToPx(value)
fun sp(value: Float): Float = spToPx(value)

fun dpToPx(dp: Float): Float {
    return dp * BaseUtils.getDisplayMetrics().density
}

fun dpToPx(dp: Int): Int {
    return (dp * BaseUtils.getDisplayMetrics().density + 0.5f).toInt()
}

fun pxToDp(px: Float): Float {
    return px / BaseUtils.getDisplayMetrics().density
}

fun pxToDp(px: Int): Int {
    return (px / BaseUtils.getDisplayMetrics().density + 0.5f).toInt()
}

fun spToPx(sp: Float): Float {
    return sp * BaseUtils.getDisplayMetrics().scaledDensity
}

fun spToPx(sp: Int): Int {
    return (sp * BaseUtils.getDisplayMetrics().scaledDensity + 0.5f).toInt()
}

fun pxToSp(px: Float): Float {
    return px / BaseUtils.getDisplayMetrics().scaledDensity
}

fun pxToSp(px: Int): Int {
    return (px / BaseUtils.getDisplayMetrics().scaledDensity + 0.5f).toInt()
}

/**
 * 各种单位转换，该方法存在于[TypedValue] 中
 *
 * @param unit  单位
 * @param value 值
 * @return 转换结果
 */
fun applyDimension(unit: Int, value: Float): Float {
    val metrics = BaseUtils.getDisplayMetrics()
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
