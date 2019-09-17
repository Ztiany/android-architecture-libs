package com.android.base.app.dagger;

import android.view.View;

import dagger.MembersInjector;

/**
 * how to use it? refer https://github.com/Ztiany/notes/blob/master/Android/00-Code/Dagger2AndroidInjection-v2.24/README.md.
 *
 * @param <A>
 */
@SuppressWarnings("WeakerAccess")
public interface ViewComponent<A extends View> extends MembersInjector<A> {
}
