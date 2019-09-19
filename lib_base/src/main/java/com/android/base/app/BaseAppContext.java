package com.android.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


public class BaseAppContext extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Sword.get().getApplicationDelegate().attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Sword.get().getApplicationDelegate().onCreate(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Sword.get().getApplicationDelegate().onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Sword.get().getApplicationDelegate().onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Sword.get().getApplicationDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Sword.get().getApplicationDelegate().onTerminate();
    }

}