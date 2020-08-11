package com.android.sdk.net;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.core.provider.ErrorDataAdapter;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.rxjava.RxResultPostTransformer;
import com.android.sdk.net.core.result.ExceptionFactory;

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
    ExceptionFactory exceptionFactory();

    @Nullable
    RxResultPostTransformer rxResultPostTransformer();

    @Nullable
    CoroutinesResultPostProcessor coroutinesResultPostProcessor();

}
