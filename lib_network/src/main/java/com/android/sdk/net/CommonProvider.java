package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.PlatformInteractor;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

public interface CommonProvider {

    @NonNull
    ErrorMessage errorMessage();

    @Nullable
    CoroutinesResultPostProcessor coroutinesResultPostProcessor();

    @Nullable
    RxResultPostTransformer<?> rxResultPostTransformer();

    @Nullable
    ErrorBodyParser errorBodyHandler();

    @NonNull
    PlatformInteractor platformInteractor();

}