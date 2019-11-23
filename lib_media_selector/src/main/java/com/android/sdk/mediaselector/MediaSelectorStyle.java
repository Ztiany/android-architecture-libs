package com.android.sdk.mediaselector;

import android.graphics.Color;

import androidx.annotation.ColorInt;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-22 11:36
 */
public class MediaSelectorStyle {

    private static int sPrimaryColor = -1;

    public static void setPrimaryColor(@ColorInt int primaryColor) {
        sPrimaryColor = primaryColor;
    }

    static int fetchPrimaryColor() {
        if (sPrimaryColor == -1) {
            return Color.WHITE;
        } else {
            return sPrimaryColor;
        }
    }

}