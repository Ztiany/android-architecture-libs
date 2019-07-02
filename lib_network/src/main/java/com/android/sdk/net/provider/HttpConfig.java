package com.android.sdk.net.provider;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:42
 */
public interface HttpConfig {

    void configHttp(OkHttpClient.Builder builder);

    /**
     * default config is {@link retrofit2.converter.gson.GsonConverterFactory}、{@link retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory} with {@link Schedulers#io()}
     *
     * @return if true, default config  do nothing.
     */
    boolean configRetrofit(Retrofit.Builder builder);

    String baseUrl();

}
