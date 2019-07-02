package com.android.base.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;

/**
 * 依赖 Content 的其他工具类都由 BaseUtils 提供
 */
public class BaseUtils {

    public static void init(Context context) {
        Utils.init(context);
    }

    public static Context getAppContext() {
        return Utils.getApp();
    }

    public static Resources getResources() {
        return BaseUtils.getAppContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return BaseUtils.getAppContext().getTheme();
    }

    public static AssetManager getAssets() {
        return BaseUtils.getAppContext().getAssets();
    }

    @SuppressWarnings("unused")
    public static Configuration getConfiguration() {
        return BaseUtils.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return BaseUtils.getResources().getDisplayMetrics();
    }

}