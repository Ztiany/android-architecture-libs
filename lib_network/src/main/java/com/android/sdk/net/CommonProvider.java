package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyHandler;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;

public interface CommonProvider {

    @NonNull
    ErrorMessage errorMessage();

    @Nullable
    CoroutinesResultPostProcessor coroutinesResultPostProcessor();

    @Nullable
    ErrorBodyHandler errorBodyHandler();

}