package com.android.sdk.net.progress;


import android.os.Handler;
import android.os.Looper;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-09-22 16:52
 */
class Dispatcher {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    static void dispatch(Runnable runnable) {
        HANDLER.post(runnable);
    }
}
