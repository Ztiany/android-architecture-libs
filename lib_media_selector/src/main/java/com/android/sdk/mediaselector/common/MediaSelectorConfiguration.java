package com.android.sdk.mediaselector.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;

import com.ztiany.mediaselector.R;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-22 11:36
 */
public class MediaSelectorConfiguration {

    private static String sAuthority = "";
    private static boolean sOpenLog = false;

    /**
     * @see <a href='https://stackoverflow.com/questions/27611173/how-to-get-accent-color-programmatically'>how-to-get-accent-color-programmatically</>
     */
    public static int getPrimaryColor(Context context) {
        TypedValue outValue = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getTheme().resolveAttribute(R.attr.colorPrimary, outValue, true);
        } else {
            // get color defined for AppCompat
            int appCompatAttribute = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
            context.getTheme().resolveAttribute(appCompatAttribute, outValue, true);
        }
        String color = String.format("#%06X", (0xFFFFFF & outValue.data));
        return Color.parseColor(color);
    }

    /**
     * 默认的 authority 为 "包名.fileProvider"
     *
     * @param authority 指定FileProvider的authority
     */
    public static void setAuthority(String authority) {
        sAuthority = authority;
    }

    public static String getAuthority(Context context) {
        if (!TextUtils.isEmpty(sAuthority)) {
            return sAuthority;
        }
        return context.getPackageName().concat(".file.provider");
    }

    public static void setOpenLog(boolean openLog) {
        sOpenLog = openLog;
    }

    public static boolean isOpenLog() {
        return sOpenLog;
    }

}