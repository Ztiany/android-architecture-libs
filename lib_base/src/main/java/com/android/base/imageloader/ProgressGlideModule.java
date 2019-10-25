package com.android.base.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

public class ProgressGlideModule extends AppGlideModule {

    //配置glide网络加载框架
    @Override
    @CallSuper
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        ProgressManager.getInstance().setRefreshTime(getRefreshTime());
        OkHttpClient.Builder builder = ProgressManager.getInstance().withProgress(new OkHttpClient.Builder());
        configImageOkHttp(builder);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }

    //不使用清单配置的方式,减少初始化时间
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    protected void configImageOkHttp(OkHttpClient.Builder builder) {

    }

    protected int getRefreshTime() {
        return 200;
    }

}