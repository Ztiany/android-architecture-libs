package com.android.base.app.dagger;

import android.view.View;

import dagger.MembersInjector;

/**
 * how to use it? refer https://github.com/Ztiany/Programming-Notes-Code/blob/master/Android/Dagger2AndroidInjection-v2.19/README.md。
 *
 * @param <A>
 */
@SuppressWarnings("WeakerAccess")
public interface ViewComponent<A extends View> extends MembersInjector<A> {
}
