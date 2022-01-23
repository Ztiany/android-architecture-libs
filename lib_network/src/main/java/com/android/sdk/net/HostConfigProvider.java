package com.android.sdk.net;

import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;

public interface HostConfigProvider {

    @Nullable
    ApiHandler aipHandler();

    @Nullable
    HttpConfig httpConfig();

    @Nullable
    ExceptionFactory exceptionFactory();

}
