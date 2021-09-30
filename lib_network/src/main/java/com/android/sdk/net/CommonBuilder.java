package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava.RxResultPostTransformer;

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

    @MainThread
    public NetContext setUp() {
        mNetContext.init(mCommonProvider);
        return mNetContext;
    }

    /**
     * If you use RxJava, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public CommonBuilder rxResultPostTransformer(@NonNull RxResultPostTransformer<?> resultPostProcessor) {
        mCommonProvider.mRxResultPostTransformer = resultPostProcessor;
        return this;
    }

    /**
     * If you use  Kotlin's Coroutines, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public CommonBuilder coroutinesResultPostProcessor(@Nullable CoroutinesResultPostProcessor resultPostProcessor) {
        mCommonProvider.mCoroutinesResultPostProcessor = resultPostProcessor;
        return this;
    }

}