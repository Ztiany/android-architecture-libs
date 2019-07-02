package com.android.base.app.dagger;

import android.view.View;

import dagger.BindsInstance;


public interface ViewComponentBuilder<T extends View, C extends ViewComponent<T>> {

    @BindsInstance
    ViewComponentBuilder bindInstance(T t);

    C build();

}