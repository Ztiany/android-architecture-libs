package com.android.sdk.net;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.NetworkChecker;

public class CommonProviderImpl implements CommonProvider {

    NetworkChecker mNetworkChecker;

    ErrorMessage mErrorMessage;

    @Override
    public boolean isConnected() {
        return mNetworkChecker.isConnected();
    }

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    void checkRequired() {
        if (mNetworkChecker == null) {
            throw new NullPointerException("You must provide following object：NetworkChecker and ErrorMessage.");
        }
    }

}
