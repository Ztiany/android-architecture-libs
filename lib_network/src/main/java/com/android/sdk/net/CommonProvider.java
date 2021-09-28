package com.android.sdk.net;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.provider.ErrorMessage;

public interface CommonProvider {

    boolean isConnected();

    @NonNull
    ErrorMessage errorMessage();

}

