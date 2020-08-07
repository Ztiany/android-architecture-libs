package com.android.sdk.net;

import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.CoroutinesRetryer;
import com.android.sdk.net.core.provider.ErrorDataAdapter;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.provider.NetworkChecker;
import com.android.sdk.net.core.provider.PostTransformer;

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
