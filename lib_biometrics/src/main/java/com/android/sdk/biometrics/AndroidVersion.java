package com.android.sdk.biometrics;

import android.os.Build;

class AndroidVersion {

    /**
     * 高于Android P（9.0）
     */
    static boolean isAboveAndroidP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    /**
     * 高于Android N（7.0）
     */
    static boolean isAboveAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 高于Android M（6.0）
     */
    static boolean isAboveAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
