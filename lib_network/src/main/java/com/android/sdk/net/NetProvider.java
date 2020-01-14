package com.android.sdk.net;

import com.android.sdk.net.core.ExceptionFactory;
import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.CoroutinesRetryer;
import com.android.sdk.net.provider.ErrorDataAdapter;
import com.android.sdk.net.provider.ErrorMessage;
import com.android.sdk.net.provider.HttpConfig;
import com.android.sdk.net.provider.NetworkChecker;
import com.android.sdk.net.provider.PostTransformer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface NetProvider {

    boolean isConnected();

    @Nullable
    ApiHandler aipHandler();

    @Nullable
    HttpConfig httpConfig();

    @NonNull
    ErrorMessage errorMessage();

    @NonNull
    ErrorDataAdapter errorDataAdapter();

    @Nullable
    PostTransformer postTransformer();

    @Nullable
    ExceptionFactory exceptionFactory();

    @Nullable
    CoroutinesRetryer coroutinesRetryer();

}

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