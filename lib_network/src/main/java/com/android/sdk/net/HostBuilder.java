package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.ErrorDataAdapter;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava.RxResultPostTransformer;

public class HostBuilder {

    private final HostNetProviderImpl mNetProvider = new HostNetProviderImpl();

    private final NetContext mNetContext;
    private final String mFlag;

    HostBuilder(String flag, NetContext netContext) {
        mNetContext = netContext;
        mFlag = flag;
    }

    public HostBuilder aipHandler(@NonNull ApiHandler apiHandler) {
        mNetProvider.mApiHandler = apiHandler;
        return this;
    }

    public HostBuilder httpConfig(@NonNull HttpConfig httpConfig) {
        mNetProvider.mHttpConfig = httpConfig;
        return this;
    }

    public HostBuilder errorMessage(@NonNull ErrorMessage errorMessage) {
        mNetProvider.mErrorMessage = errorMessage;
        return this;
    }

    public HostBuilder errorDataAdapter(@NonNull ErrorDataAdapter errorDataAdapter) {
        mNetProvider.mErrorDataAdapter = errorDataAdapter;
        return this;
    }

    public HostBuilder exceptionFactory(@NonNull ExceptionFactory exceptionFactory) {
        mNetProvider.mExceptionFactory = exceptionFactory;
        return this;
    }

    /**
     * If you use RxJava, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public HostBuilder rxRetrier(@NonNull RxResultPostTransformer<?> retrier) {
        mNetProvider.mRxResultPostTransformer = retrier;
        return this;
    }

    /**
     * If you use  Kotlin's Coroutines, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public HostBuilder coroutinesRetrier(@Nullable CoroutinesResultPostProcessor retrier) {
        mNetProvider.mCoroutinesResultPostProcessor = retrier;
        return this;
    }

    @MainThread
    public void setup() {
        mNetProvider.checkRequired();
        mNetContext.addInto(mFlag, mNetProvider);
    }

}