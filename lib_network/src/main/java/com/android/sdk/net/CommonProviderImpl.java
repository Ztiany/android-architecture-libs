package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyHandler;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;

public class CommonProviderImpl implements CommonProvider {

    ErrorMessage mErrorMessage;

    CoroutinesResultPostProcessor mCoroutinesResultPostProcessor;

    ErrorBodyHandler mErrorBodyHandler;

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
    public ErrorBodyHandler errorBodyHandler() {
        return mErrorBodyHandler;
    }

}