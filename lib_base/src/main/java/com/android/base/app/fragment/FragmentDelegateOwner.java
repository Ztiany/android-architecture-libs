package com.android.base.app.fragment;

import android.support.annotation.UiThread;

import com.github.dmstocking.optional.java.util.function.Predicate;

@UiThread
public interface FragmentDelegateOwner {

    void addDelegate(FragmentDelegate fragmentDelegate);

    boolean removeDelegate(FragmentDelegate fragmentDelegate);

    FragmentDelegate findDelegate(Predicate<FragmentDelegate> predicate);

}
