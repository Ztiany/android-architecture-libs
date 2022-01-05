package com.android.sdk.net.core.provider;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:42
 */
public interface HttpConfig {

    /**
     * base url for build retrofit.
     */
    String baseUrl();

    /**
     * config OkHttp client.
     */
    void configHttp(@NonNull OkHttpClient.Builder builder);

    /**
     * default config is {@link retrofit2.converter.gson.GsonConverterFactory}}
     *
     * @return if true, default config  do nothing, and all config up to you.
     */
    boolean configRetrofit(@NonNull OkHttpClient okHttpClient, @NonNull Retrofit.Builder builder);

}
