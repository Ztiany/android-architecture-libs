package com.android.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


public class BaseAppContext extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BaseKit.get().getApplicationDelegate().attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseKit.get().getApplicationDelegate().onCreate(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        BaseKit.get().getApplicationDelegate().onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        BaseKit.get().getApplicationDelegate().onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        BaseKit.get().getApplicationDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BaseKit.get().getApplicationDelegate().onTerminate();
    }

}