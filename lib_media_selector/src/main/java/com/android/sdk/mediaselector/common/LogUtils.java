package com.android.sdk.mediaselector.common;

import android.util.Log;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-07 10:19
 */
public class LogUtils {

    private static final String TAG = "MediaSelector";

    public static void d(Object message) {
        if (MediaSelectorConfiguration.isOpenLog()) {
            Log.d(TAG, message == null ? "null" : message.toString());
        }
    }

    public static void e(Object message, Throwable throwable) {
        if (MediaSelectorConfiguration.isOpenLog()) {
            Log.e(TAG, message == null ? "null" : message.toString(), throwable);
        }
    }

}