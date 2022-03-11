package com.android.sdk.net.core.progress;


import android.os.Handler;
import android.os.Looper;

/**
 * @author Ztiany
 * Date : 2017-09-22 16:52
 */
class Dispatcher {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    static void dispatch(Runnable runnable) {
        HANDLER.post(runnable);
    }
}
