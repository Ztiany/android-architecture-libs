package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.NetworkChecker;

public class CommonBuilder {

    private final NetContext mNetContext;

    CommonBuilder(NetContext netContext) {
        mNetContext = netContext;
    }

    private final CommonProviderImpl mCommonProvider = new CommonProviderImpl();

    /**
     * Set a global ErrorMessage, which is optional.
     */
    public CommonBuilder errorMessage(@NonNull ErrorMessage errorMessage) {
        mCommonProvider.mErrorMessage = errorMessage;
        return this;
    }

    public CommonBuilder networkChecker(@NonNull NetworkChecker networkChecker) {
        mCommonProvider.mNetworkChecker = networkChecker;
        return this;
    }

    @MainThread
    public NetContext setUp() {
        mNetContext.init(mCommonProvider);
        return mNetContext;
    }

}
