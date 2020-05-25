package com.android.sdk.social.common;

import android.text.TextUtils;


public class Utils {

    private Utils() {
    }

    public static void requestNotNull(String str, String errorMsg) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException(errorMsg);
        }
    }

}
