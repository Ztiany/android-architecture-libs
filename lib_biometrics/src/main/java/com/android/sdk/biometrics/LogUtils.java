package com.android.sdk.biometrics;

import android.util.Log;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 17:39
 */
class LogUtils {

    private static final String TAG = "BiometricsAndroid";

    static void log(CharSequence message) {
        Log.d(TAG, message.toString());
    }

}
