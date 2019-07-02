package com.android.base.app.dagger;

import android.view.View;

import java.util.Map;

import javax.inject.Provider;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-06-21 14:58
 */
public interface HasViewInjector {

    Map<Class<? extends View>, Provider<ViewComponentBuilder>> viewInjectors();

}
