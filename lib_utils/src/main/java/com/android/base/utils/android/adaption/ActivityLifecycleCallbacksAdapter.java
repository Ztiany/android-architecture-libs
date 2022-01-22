package com.android.base.utils.android.adaption;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public interface ActivityLifecycleCallbacksAdapter extends Application.ActivityLifecycleCallbacks {

    @Override
    default void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    default void onActivityStarted(Activity activity) {
    }

    @Override
    default void onActivityResumed(Activity activity) {
    }

    @Override
    default void onActivityPaused(Activity activity) {
    }

    @Override
    default void onActivityStopped(Activity activity) {
    }

    @Override
    default void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    default void onActivityDestroyed(Activity activity) {
    }

}
