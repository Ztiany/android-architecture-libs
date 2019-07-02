package com.android.sdk.net.provider;

import android.support.annotation.NonNull;

import com.android.sdk.net.core.Result;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 15:37
 */
public interface ApiHandler {

    void onApiError(@NonNull Result result);

}
