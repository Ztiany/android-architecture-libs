package com.android.sdk.net;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.service.ServiceFactory;
import com.android.sdk.net.core.service.ServiceHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 11:06
 */
public class NetContext {

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

    static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";

    private NetContext() {
        mServiceHelper = new ServiceHelper();
    }

    private final ServiceHelper mServiceHelper;

    private CommonProvider mCommonProvider;

    private final Map<String, HostNetProvider> mProviderMap = new HashMap<>();

    public void init(CommonProvider commonProvider) {
        mCommonProvider = commonProvider;
    }

    void addInto(String flag, @NonNull HostNetProvider hostNetProvider) {
        mProviderMap.put(flag, hostNetProvider);
    }

    public CommonBuilder commonConfig() {
        return new CommonBuilder(this);
    }

    public HostBuilder addBuilder() {
        return addBuilder(DEFAULT_CONFIG);
    }

    public HostBuilder addBuilder(String flag) {
        checkIfHasBeenInitialized();
        return new HostBuilder(flag, this);
    }

    private void checkIfHasBeenInitialized() {
        if (mCommonProvider == null) {
            throw new IllegalStateException("You should set common configurations by calling commonConfig() first.");
        }
    }

    public boolean isConnected() {
        return mCommonProvider.isConnected();
    }

    public HostNetProvider netProvider() {
        return netProvider(DEFAULT_CONFIG);
    }

    public HostNetProvider netProvider(String flag) {
        HostNetProvider hostNetProvider = mProviderMap.get(flag);

        if (hostNetProvider == null) {
            throw new RuntimeException("The HostNetProvider identified as " + flag + " has not been initialized");
        }

        return hostNetProvider;
    }

    public OkHttpClient httpClient() {
        return httpClient(DEFAULT_CONFIG);
    }

    public OkHttpClient httpClient(String flag) {
        return mServiceHelper.getOkHttpClient(flag, netProvider(flag).httpConfig());
    }

    public ServiceFactory serviceFactory() {
        return serviceFactory(DEFAULT_CONFIG);
    }

    public ServiceFactory serviceFactory(String flag) {
        return mServiceHelper.getServiceFactory(flag, netProvider(flag).httpConfig());
    }

}
