package com.android.sdk.push;

import android.app.Application;
import android.text.TextUtils;

import com.android.sdk.push.jpush.JPush;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-03-03 16:22
 */
public class PushContext {

    private static Application mApplication;
    private static boolean isDebug;
    private static Push sPush;

    private static String APP_ID;
    private static String APP_KEY;

    public static void configPush(String appKey, String appId) {
        APP_KEY = appKey;
        APP_ID = appId;
    }

    public static boolean isPushConfigured() {
        return !TextUtils.isEmpty(APP_ID) && !TextUtils.isEmpty(APP_KEY);
    }

    public static void init(Application application, boolean debug) {
        isDebug = debug;
        mApplication = application;
        initPush();
    }

    private static void initPush() {
        sPush = new JPush(mApplication);
    }

    public static Push getPush() {
        return sPush;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static Application getApplication() {
        return mApplication;
    }

}
