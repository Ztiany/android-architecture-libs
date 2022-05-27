package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.PlatformInteractor;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

public class CommonProviderImpl implements CommonProvider {

    ErrorMessage mErrorMessage;

    CoroutinesResultPostProcessor mCoroutinesResultPostProcessor;

    ErrorBodyParser mErrorBodyParser;

    PlatformInteractor mPlatformInteractor;

    RxResultPostTransformer<?> mRxResultPostTransformer;

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @Nullable
    @Override
    public CoroutinesResultPostProcessor coroutinesResultPostProcessor() {
        return mCoroutinesResultPostProcessor;
    }

    @Nullable
    @Override
    public RxResultPostTransformer<?> rxResultPostTransformer() {
        return mRxResultPostTransformer;
    }

    @Nullable
    @Override
    public ErrorBodyParser errorBodyHandler() {
        return mErrorBodyParser;
    }

    @NonNull
    @Override
    public PlatformInteractor platformInteractor() {
        return mPlatformInteractor;
    }

    public void checkRequirement() {
        if (mPlatformInteractor == null || mErrorMessage == null) {
            throw new NullPointerException("You must provide following object：ErrorMessage, PlatformInteractor.");
        }
    }

}