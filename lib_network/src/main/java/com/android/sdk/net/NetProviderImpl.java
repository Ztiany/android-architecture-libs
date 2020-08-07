package com.android.sdk.net;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.CoroutinesRetryer;
import com.android.sdk.net.core.provider.ErrorDataAdapter;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.provider.NetworkChecker;
import com.android.sdk.net.core.provider.PostTransformer;
import com.android.sdk.net.core.result.ExceptionFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class NetProviderImpl implements NetProvider {

    ExceptionFactory mExceptionFactory;
    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;
    ErrorMessage mErrorMessage;
    ErrorDataAdapter mErrorDataAdapter;
    NetworkChecker mNetworkChecker;
    PostTransformer mPostTransformer;
    CoroutinesRetryer mCoroutinesRetryer;

    @Override
    public boolean isConnected() {
        return mNetworkChecker.isConnected();
    }

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
    public PostTransformer postTransformer() {
        return mPostTransformer;
    }

    @Nullable
    @Override
    public ExceptionFactory exceptionFactory() {
        return mExceptionFactory;
    }

    @Nullable
    @Override
    public CoroutinesRetryer coroutinesRetryer() {
        return mCoroutinesRetryer;
    }

    void checkRequired() {
        if (mErrorMessage == null || mErrorDataAdapter == null || mNetworkChecker == null || mHttpConfig == null) {
            throw new NullPointerException("You must provide following object：ErrorMessage, mErrorDataAdapter, mNetworkChecker, HttpConfig.");
        }
    }

}