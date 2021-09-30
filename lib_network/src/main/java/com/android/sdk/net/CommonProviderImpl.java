package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava.RxResultPostTransformer;

public class CommonProviderImpl implements CommonProvider {

    ErrorMessage mErrorMessage;

    RxResultPostTransformer<?> mRxResultPostTransformer;

    CoroutinesResultPostProcessor mCoroutinesResultPostProcessor;

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @Nullable
    @Override
    public RxResultPostTransformer<?> rxResultPostTransformer() {
        return mRxResultPostTransformer;
    }

    @Nullable
    @Override
    public CoroutinesResultPostProcessor coroutinesResultPostProcessor() {
        return mCoroutinesResultPostProcessor;
    }

}