package com.android.base.utils.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.android.base.utils.BaseUtils;


public class ResourceUtils {

    private ResourceUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    /**
     * @param name       资源的名称，如 ic_launcher 或者 com.example.android/drawable/ic_launcher(这是，下面两个参数可以省略)
     * @param defType    资源的类型，如 drawable
     * @param defPackage 包名
     * @return 资源id
     */
    public static int getResource(String name, String defType, String defPackage) {
        return BaseUtils.getResources().getIdentifier(name, defType, defPackage);
    }

    public static CharSequence getText(@StringRes int id) {
        return BaseUtils.getResources().getText(id);
    }

    public static String getString(@StringRes int id) {
        return BaseUtils.getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return BaseUtils.getResources().getString(id, formatArgs);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return BaseUtils.getResources().getStringArray(id);
    }


    public static int[] getIntArray(@ArrayRes int id) {
        return BaseUtils.getResources().getIntArray(id);
    }


    public static Uri createUriByResource(int id) {
        return Uri.parse("android.resource://" + BaseUtils.getAppContext().getPackageName() + "/" + id);
    }

    public static Uri createUriByAssets(String path) {
        return Uri.parse("file:///android_asset/" + path);
    }

    public static int getStyledColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0x000000);
        } finally {
            a.recycle();
        }
    }

    public static Drawable getStyledDrawable(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getDrawable(0);
        } finally {
            a.recycle();
        }
    }

    public static int getDimensPixelSize(int dimenId) {
        return BaseUtils.getResources().getDimensionPixelSize(dimenId);
    }
}
