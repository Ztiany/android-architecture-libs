package com.android.sdk.net.service;

import com.android.sdk.net.gson.ErrorJsonLenientConverterFactory;
import com.android.sdk.net.gson.GsonUtils;
import com.android.sdk.net.progress.RequestProgressInterceptor;
import com.android.sdk.net.progress.ResponseProgressInterceptor;
import com.android.sdk.net.progress.UrlProgressListener;
import com.android.sdk.net.provider.HttpConfig;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-06-07 18:19
 */
public class ServiceFactory {

    private final OkHttpClient mOkHttpClient;
    private final String mBaseUrl;
    private final Retrofit mRetrofit;

    ServiceFactory(OkHttpClient okHttpClient, HttpConfig httpConfig) {
        mOkHttpClient = okHttpClient;
        mBaseUrl = httpConfig.baseUrl();

        Retrofit.Builder builder = new Retrofit.Builder();
        boolean abort = httpConfig.configRetrofit(builder);

        if (!abort) {
            builder.baseUrl(mBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(new ErrorJsonLenientConverterFactory(GsonConverterFactory.create(GsonUtils.gson())))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        }

        mRetrofit = builder.build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public <T> T createWithUploadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new RequestProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public <T> T createWithDownloadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new ResponseProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public String baseUrl() {
        return mBaseUrl;
    }

}
