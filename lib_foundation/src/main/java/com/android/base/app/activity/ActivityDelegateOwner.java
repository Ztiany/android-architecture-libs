package com.android.base.app.activity;


import com.github.dmstocking.optional.java.util.function.Predicate;

import androidx.annotation.UiThread;

@UiThread
@SuppressWarnings("unused")
public interface ActivityDelegateOwner {

    void addDelegate(ActivityDelegate fragmentDelegate);

    boolean removeDelegate(ActivityDelegate fragmentDelegate);

    ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate);

    ActivityState getStatus();

}