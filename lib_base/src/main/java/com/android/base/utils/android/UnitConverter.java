package com.android.base.utils.android;


import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.android.base.utils.BaseUtils;

public class UnitConverter {

    private UnitConverter() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    public static float dpToPx(float dp) {
        return dp * BaseUtils.getDisplayMetrics().density;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * BaseUtils.getDisplayMetrics().density + 0.5f);
    }

    public static float pxToDp(float px) {
        return px / BaseUtils.getDisplayMetrics().density;
    }

    public static int pxToDp(int px) {
        return (int) (px / BaseUtils.getDisplayMetrics().density + 0.5f);
    }

    public static float spToPx(float sp) {
        return sp * BaseUtils.getDisplayMetrics().scaledDensity;
    }

    public static int spToPx(int sp) {
        return (int) (sp * BaseUtils.getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static float pxToSp(float px) {
        return px / BaseUtils.getDisplayMetrics().scaledDensity;
    }

    public static int pxToSp(int px) {
        return (int) (px / BaseUtils.getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * 各种单位转换，该方法存在于{@link TypedValue} 中
     *
     * @param unit  单位
     * @param value 值
     * @return 转换结果
     */
    public static float applyDimension(int unit, float value) {
        DisplayMetrics metrics = BaseUtils.getDisplayMetrics();
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }
}