package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.result.Result;

import java.util.ArrayList;
import java.util.List;

public class HostConfigBuilder {

    private final HostConfigProviderImpl mNetProvider = new HostConfigProviderImpl();

    private final NetContext mNetContext;

    private final String mFlag;

    private List<Class<? extends Result<?>>> mResultTypeList;

    HostConfigBuilder(@NonNull String flag, @NonNull NetContext netContext) {
        mNetContext = netContext;
        mFlag = flag;
    }

    public HostConfigBuilder aipHandler(@NonNull ApiHandler apiHandler) {
        mNetProvider.mApiHandler = apiHandler;
        return this;
    }

    public HostConfigBuilder httpConfig(@NonNull HttpConfig httpConfig) {
        mNetProvider.mHttpConfig = httpConfig;
        return this;
    }

    public HostConfigBuilder exceptionFactory(@NonNull ExceptionFactory exceptionFactory) {
        mNetProvider.mExceptionFactory = exceptionFactory;
        return this;
    }

    public HostConfigBuilder registerResult(Class<? extends Result<?>> clazz) {
        if (mResultTypeList == null) {
            mResultTypeList = new ArrayList<>();
        }
        mResultTypeList.add(clazz);
        return this;
    }

    @MainThread
    public NetContext setUp() {
        mNetProvider.checkRequired();

        if (mResultTypeList != null) {
            for (Class<? extends Result<?>> aClass : mResultTypeList) {
                mNetContext.getHostFlagHolder().registerType(aClass, mFlag);
            }
        }

        mNetContext.addInto(mFlag, mNetProvider);
        return mNetContext;
    }

}