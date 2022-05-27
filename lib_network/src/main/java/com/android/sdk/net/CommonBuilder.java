package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.PlatformInteractor;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

public class CommonBuilder {

    private final NetContext mNetContext;

    CommonBuilder(NetContext netContext) {
        mNetContext = netContext;
    }

    private final CommonProviderImpl mCommonProvider = new CommonProviderImpl();

    public CommonBuilder errorMessage(@NonNull ErrorMessage errorMessage) {
        mCommonProvider.mErrorMessage = errorMessage;
        return this;
    }

    public CommonBuilder errorBodyHandler(@NonNull ErrorBodyParser errorBodyParser) {
        mCommonProvider.mErrorBodyParser = errorBodyParser;
        return this;
    }

    public CommonBuilder platformInteractor(@NonNull PlatformInteractor platformInteractor) {
        mCommonProvider.mPlatformInteractor = platformInteractor;
        return this;
    }

    /**
     * You can use this to set up a piece of logic that will be executed before retrial.
     */
    public CommonBuilder coroutinesResultPostProcessor(@Nullable CoroutinesResultPostProcessor resultPostProcessor) {
        mCommonProvider.mCoroutinesResultPostProcessor = resultPostProcessor;
        return this;
    }

    /**
     * If you use RxJava2, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public CommonBuilder rx2ResultPostTransformer(@NonNull RxResultPostTransformer<?> resultPostProcessor) {
        mCommonProvider.mRxResultPostTransformer = resultPostProcessor;
        return this;
    }

    @MainThread
    public NetContext setUp() {
        mCommonProvider.checkRequirement();
        mNetContext.initCommonProvider(mCommonProvider);
        return mNetContext;
    }

}