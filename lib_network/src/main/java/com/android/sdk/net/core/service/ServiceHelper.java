package com.android.sdk.net.core.service;

import com.android.sdk.net.core.provider.HttpConfig;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:51
 */
public class ServiceHelper {

    private final Map<String, OkHttpClient> mOkHttpClientMap = new HashMap<>();

    private final Map<String, ServiceFactory> mServiceFactoryMap = new HashMap<>();

    public OkHttpClient getOkHttpClient(String flag, HttpConfig httpConfig) {
        OkHttpClient httpClient = mOkHttpClientMap.get(flag);

        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (httpConfig != null) {
                httpConfig.configHttp(builder);
            }
            httpClient = builder.build();

            mOkHttpClientMap.put(flag, httpClient);
        }

        return httpClient;
    }

    public ServiceFactory getServiceFactory(String flag, HttpConfig httpConfig) {
        ServiceFactory serviceFactory = mServiceFactoryMap.get(flag);

        if (serviceFactory == null) {
            serviceFactory = new ServiceFactory(getOkHttpClient(flag, httpConfig), httpConfig);
            mServiceFactoryMap.put(flag, serviceFactory);
        }

        return serviceFactory;
    }

}
