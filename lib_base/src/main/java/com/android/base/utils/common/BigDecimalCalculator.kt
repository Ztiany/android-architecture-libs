package com.android.base.utils.common

import java.math.BigDecimal

/*使用 BigDecimal 对进行金额计算 */

val ZERO = BigDecimal("0")

private const val SCALE = 2

fun add(value1: BigDecimal, value2: Float): BigDecimal {
    return value1.add(BigDecimal(value2.toString()))
}

fun add(value1: Float, value2: Float): BigDecimal {
    return BigDecimal(value1.toString()).add(BigDecimal(value2.toString()))
}

fun multiply(value1: BigDecimal, value2: Float): BigDecimal {
    return value1.multiply(BigDecimal(value2.toString()))
}

fun multiply(value1: Float, value2: Float): BigDecimal {
    return BigDecimal(value1.toString()).multiply(BigDecimal(value2.toString()))
}

fun subtract(value1: BigDecimal, value2: Float): BigDecimal {
    return value1.subtract(BigDecimal(value2.toString()))
}

fun subtract(value1: Float, value2: Float): BigDecimal {
    return BigDecimal(value1.toString()).subtract(BigDecimal(value2.toString()))
}

fun divide(dividend: BigDecimal, divisor: BigDecimal?): BigDecimal {
    return dividend.divide(divisor, SCALE, BigDecimal.ROUND_HALF_UP)
}

fun divide(dividend: Float, divisor: BigDecimal?): BigDecimal {
    return BigDecimal(dividend.toString()).divide(divisor, SCALE, BigDecimal.ROUND_HALF_UP)
}

fun divide(dividend: BigDecimal, divisor: Float): BigDecimal {
    return dividend.divide(BigDecimal(divisor.toString()), SCALE, BigDecimal.ROUND_HALF_UP)
}

fun divide(dividend: Float, divisor: Float): BigDecimal {
    return BigDecimal(dividend.toString()).divide(BigDecimal(divisor.toString()), SCALE, BigDecimal.ROUND_HALF_UP)
}

/**
 * 提供精确的小数位四舍五入处理。
 *
 * @param v 需要四舍五入的数字
 * @return 四舍五入后的结果
 */
fun round(v: Float): Float {
    val b = BigDecimal(v.toString())
    val one = BigDecimal("1")
    return b.divide(one, SCALE, BigDecimal.ROUND_HALF_UP).toFloat()
}

fun toBigDecimal(price: Float): BigDecimal {
    return BigDecimal(price.toString())
}

fun isZero(value: Float): Boolean {
    return value.compareTo(0f) == 0
}

fun equals(a: Float, b: Float): Boolean {
    return a.compareTo(b) == 0
}

/**
 * @return a > b return true or false
 */
fun isGreaterThan(a: Float, b: Float): Boolean {
    return a.compareTo(b) == 1
}

/**
 * @return a >= b true or false
 */
fun isEqualsOrGreaterThan(a: Float, b: Float): Boolean {
    val compare = a.compareTo(b)
    return compare != -1
}

/**
 * @return true a < b or false
 */
fun isLessThan(a: Float, b: Float): Boolean {
    return a.compareTo(b) == -1
}

/**
 * @return a <= b true or false
 */
fun isEqualsOrLessThan(a: Float, b: Float): Boolean {
    val compare = a.compareTo(b)
    return compare != 1
}