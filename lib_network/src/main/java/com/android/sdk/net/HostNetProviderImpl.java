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

class HostNetProviderImpl implements HostNetProvider {

    ExceptionFactory mExceptionFactory;
    ErrorMessage mErrorMessage;

    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;
    ErrorDataAdapter mErrorDataAdapter;
    RxResultPostTransformer<?> mRxResultPostTransformer;
    CoroutinesResultPostProcessor mCoroutinesResultPostProcessor;

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
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @NonNull
    @Override
    public ErrorDataAdapter errorDataAdapter() {
        return mErrorDataAdapter;
    }

    @Nullable
    @Override
    public RxResultPostTransformer<?> rxResultPostTransformer() {
        return mRxResultPostTransformer;
    }

    @Nullable
    @Override
    public ExceptionFactory exceptionFactory() {
        return mExceptionFactory;
    }

    @Nullable
    @Override
    public CoroutinesResultPostProcessor coroutinesResultPostProcessor() {
        return mCoroutinesResultPostProcessor;
    }

    void checkRequired() {
        if (mErrorMessage == null || mErrorDataAdapter == null || mHttpConfig == null) {
            throw new NullPointerException("You must provide following object：ErrorMessage, mErrorDataAdapter, mNetworkChecker, HttpConfig.");
        }
    }

}