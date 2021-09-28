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

public interface HostNetProvider {

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
    RxResultPostTransformer<?> rxResultPostTransformer();

    @Nullable
    CoroutinesResultPostProcessor coroutinesResultPostProcessor();

}
