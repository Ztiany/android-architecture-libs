package com.android.sdk.net.service;

import com.android.sdk.net.provider.HttpConfig;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:51
 */
public class ServiceHelper {

    private OkHttpClient mOkHttpClient;
    private ServiceFactory mServiceFactory;

    public OkHttpClient getOkHttpClient(HttpConfig httpConfig) {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            httpConfig.configHttp(builder);
            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    public ServiceFactory getServiceFactory(HttpConfig httpConfig) {
        if (mServiceFactory == null) {
            mServiceFactory = new ServiceFactory(getOkHttpClient(httpConfig), httpConfig);
        }
        return mServiceFactory;
    }

}
