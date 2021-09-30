package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.ErrorDataAdapter;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava.RxResultPostTransformer;

class HostConfigProviderImpl implements HostConfigProvider {

    ExceptionFactory mExceptionFactory;
    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;
    ErrorDataAdapter mErrorDataAdapter;

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

    @NonNull
    @Override
    public ErrorDataAdapter errorDataAdapter() {
        return mErrorDataAdapter;
    }

    @Nullable
    @Override
    public ExceptionFactory exceptionFactory() {
        return mExceptionFactory;
    }


    void checkRequired() {
        if (mErrorDataAdapter == null || mHttpConfig == null) {
            throw new NullPointerException("You must provide following object：ErrorMessage, mErrorDataAdapter, mNetworkChecker, HttpConfig.");
        }
    }

}