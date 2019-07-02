package com.android.base.app.dagger;

import android.view.View;

import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.internal.Beta;

import static java.lang.annotation.ElementType.METHOD;

@Beta
@MapKey
@Target(METHOD)
@SuppressWarnings("unused")
public @interface ViewKey {
    Class<? extends View> value();
}
