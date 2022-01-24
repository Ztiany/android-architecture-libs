package com.android.sdk.net;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.sdk.net.core.host.HostFlagHolder;
import com.android.sdk.net.core.message.ErrorMessageFactory;
import com.android.sdk.net.core.service.ServiceFactory;
import com.android.sdk.net.core.service.ServiceHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 11:06
 */
public class NetContext {

    @SuppressLint("StaticFieldLeak")
    private static volatile NetContext CONTEXT;

    public static NetContext get() {
        if (CONTEXT == null) {
            synchronized (NetContext.class) {
                if (CONTEXT == null) {
                    CONTEXT = new NetContext();
                }
            }
        }
        return CONTEXT;
    }

    public static final String DEFAULT_FLAG = "DEFAULT_CONFIG";

    private final ServiceHelper mServiceHelper;

    private Context mContext;

    private final HostFlagHolder mHostFlagHolder = new HostFlagHolder();

    private final Map<String, HostConfigProvider> mProviderMap = new HashMap<>();

    private CommonProvider mCommonProvider;

    private NetContext() {
        mServiceHelper = new ServiceHelper();
    }

    void initCommonProvider(CommonProvider commonProvider) {
        mCommonProvider = commonProvider;
    }

    @MainThread
    public CommonBuilder newCommonConfig(Context context) {
        mContext = context;
        return new CommonBuilder(this);
    }

    @MainThread
    public HostConfigBuilder newHostBuilder() {
        return newHostBuilder(DEFAULT_FLAG);
    }

    @MainThread
    public HostConfigBuilder newHostBuilder(@NonNull String flag) {
        checkIfHasBeenInitialized();
        return new HostConfigBuilder(flag, this);
    }

    void addInto(String flag, @NonNull HostConfigProvider hostConfigProvider) {
        mProviderMap.put(flag, hostConfigProvider);
    }

    private void checkIfHasBeenInitialized() {
        if (mContext == null) {
            throw new IllegalStateException("You should call commonConfig() then setUp() first.");
        }
    }

    public CommonProvider commonProvider() {
        return mCommonProvider;
    }

    public boolean isConnected() {
        return commonProvider().platformInteractor().isConnected();
    }

    public HostConfigProvider hostConfigProvider() {
        return hostConfigProvider(DEFAULT_FLAG);
    }

    public HostConfigProvider hostConfigProviderByResultType(Type type) {
        return hostConfigProvider(getHostFlagHolder().getFlag(type));
    }

    public HostConfigProvider hostConfigProvider(String flag) {
        HostConfigProvider hostConfigProvider = mProviderMap.get(flag);

        if (hostConfigProvider == null) {
            throw new RuntimeException("The HostNetProvider identified as " + flag + " has not been initialized");
        }

        return hostConfigProvider;
    }

    public OkHttpClient httpClient() {
        return httpClient(DEFAULT_FLAG);
    }

    public OkHttpClient httpClient(String flag) {
        return mServiceHelper.getOkHttpClient(flag, hostConfigProvider(flag).httpConfig());
    }

    public ServiceFactory serviceFactory() {
        return serviceFactory(DEFAULT_FLAG);
    }

    public ServiceFactory serviceFactory(String flag) {
        return mServiceHelper.getServiceFactory(flag, hostConfigProvider(flag).httpConfig());
    }

    public Context getContext() {
        return mContext;
    }

    public HostFlagHolder getHostFlagHolder() {
        return mHostFlagHolder;
    }

    public CharSequence createMessage(Throwable exception) {
        return ErrorMessageFactory.createMessage(exception);
    }

}
