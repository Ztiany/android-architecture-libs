package com.android.base.app.activity;

import android.support.annotation.UiThread;

import com.github.dmstocking.optional.java.util.function.Predicate;

@UiThread
@SuppressWarnings("unused")
public interface ActivityDelegateOwner {

    void addDelegate(ActivityDelegate fragmentDelegate);

    boolean removeDelegate(ActivityDelegate fragmentDelegate);

    ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate);

    ActivityStatus getStatus();

}
