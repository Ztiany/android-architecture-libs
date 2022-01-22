package com.android.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;

/**
 * 依赖 Context 的其他工具类都由 BaseUtils 提供
 */
public class BaseUtils {

    private static Application sApplication;

    public static void init(Application application) {
        Utils.init(application);
        sApplication = application;
    }

    public static Context getAppContext() {
        return sApplication;
    }

    public static Resources getResources() {
        return sApplication.getResources();
    }

    public static AssetManager getAssets() {
        return sApplication.getAssets();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return sApplication.getResources().getDisplayMetrics();
    }

}