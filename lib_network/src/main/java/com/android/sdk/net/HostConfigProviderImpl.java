package com.android.sdk.net;

import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;

class HostConfigProviderImpl implements HostConfigProvider {

    ExceptionFactory mExceptionFactory;
    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;

    @Nullable
    @Override
    public ApiHandler aipHandler() {
        return mApiHandler;
    }

    @Nullable
    @Override
    public HttpConfig httpConfig() {
        return mHttpConfig;
    }

    @Nullable
    @Override
    public ExceptionFactory exceptionFactory() {
        return mExceptionFactory;
    }

    void checkRequired() {
        if (mHttpConfig == null) {
            throw new NullPointerException("You must provide following object：HttpConfig.");
        }
    }

}