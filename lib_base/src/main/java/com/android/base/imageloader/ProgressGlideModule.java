package com.android.base.imageloader;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

public class ProgressGlideModule extends AppGlideModule {

    @Override
    @CallSuper
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //配置glide网络加载框架
        ProgressManager.getInstance().setRefreshTime(getRefreshTime());
        OkHttpClient.Builder builder = ProgressManager.getInstance().withProgress(new OkHttpClient.Builder());
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //不使用清单配置的方式,减少初始化时间
        return false;
    }

    protected int getRefreshTime() {
        return 200;
    }

}