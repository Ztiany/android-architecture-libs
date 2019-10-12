package com.android.base.app.fragment.delegates;


import com.github.dmstocking.optional.java.util.function.Predicate;

import androidx.annotation.UiThread;

@UiThread
public interface FragmentDelegateOwner {

    void addDelegate(FragmentDelegate fragmentDelegate);

    boolean removeDelegate(FragmentDelegate fragmentDelegate);

    FragmentDelegate findDelegate(Predicate<FragmentDelegate> predicate);

}
