package com.android.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.base.utils.android.AppUtils;
import com.android.base.utils.android.network.NetworkStateKt;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 依赖 Context 的其他工具类都由 BaseUtils 提供
 */
public class BaseUtils {

    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static Application sApplication;

    public static void init(Application application) {
        if (isInitialized.compareAndSet(false, true)) {
            sApplication = application;
            AppUtils.registerActivityLifecycle(application);
            NetworkStateKt.initNetworkState(application);
        }
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