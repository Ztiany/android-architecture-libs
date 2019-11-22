package com.android.sdk.mediaselector;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-22 11:36
 */
class StyleUtils {

    //https://stackoverflow.com/questions/27611173/how-to-get-accent-color-programmatically
    static int fetchPrimaryColor(Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorPrimary;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

}