package com.android.base.utils.common;

import java.math.BigDecimal;

/**
 * 使用 BigDecimal 计算
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-06 14:58
 */
@SuppressWarnings("unused")
public class BigDecimalCalculator {

    public static final BigDecimal ZERO = new BigDecimal("0");
    private static final int SCALE = 2;

    private BigDecimalCalculator() {
        throw new UnsupportedOperationException();
    }

    public static BigDecimal add(BigDecimal value1, float value2) {
        return value1.add(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal add(float value1, float value2) {
        return new BigDecimal(Float.toString(value1)).add(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal multiply(BigDecimal value1, float value2) {
        return value1.multiply(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal multiply(float value1, float value2) {
        return new BigDecimal(Float.toString(value1)).multiply(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal subtract(BigDecimal value1, float value2) {
        return value1.subtract(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal subtract(float value1, float value2) {
        return new BigDecimal(Float.toString(value1)).subtract(new BigDecimal(Float.toString(value2)));
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(float dividend, BigDecimal divisor) {
        return new BigDecimal(Float.toString(dividend)).divide(divisor, SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(BigDecimal dividend, float divisor) {
        return dividend.divide(new BigDecimal(Float.toString(divisor)), SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(float dividend, float divisor) {
        return new BigDecimal(Float.toString(dividend)).divide(new BigDecimal(Float.toString(divisor)), SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @return 四舍五入后的结果
     */
    public static float round(float v) {
        BigDecimal b = new BigDecimal(Float.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, SCALE, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static BigDecimal toBigDecimal(float price) {
        return new BigDecimal(String.valueOf(price));
    }

    public static boolean isZero(float value) {
        return Float.compare(value, 0F) == 0;
    }

    public static boolean equals(float a, float b) {
        return Float.compare(a, b) == 0;
    }

    /**
     * @return a > b return true or false
     */
    public static boolean isGreaterThan(float a, float b) {
        return Float.compare(a, b) == 1;
    }

    /**
     * @return a >= b true or false
     */
    public static boolean isEqualsOrGreaterThan(float a, float b) {
        int compare = Float.compare(a, b);
        return compare != -1;
    }

    /**
     * @return true a < b or false
     */
    public static boolean isLessThan(float a, float b) {
        return Float.compare(a, b) == -1;
    }

    /**
     * @return a <= b true or false
     */
    public static boolean isEqualsOrLessThan(float a, float b) {
        int compare = Float.compare(a, b);
        return compare != 1;
    }

}