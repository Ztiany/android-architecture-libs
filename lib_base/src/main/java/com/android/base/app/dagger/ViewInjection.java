package com.android.base.app.dagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.Map;

import javax.inject.Provider;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-06-21 14:55
 */
@SuppressWarnings("unused")
public class ViewInjection {

    private static final String TAG = ViewInjection.class.getSimpleName();

    @SuppressWarnings("unchecked")
    public static void inject(View view) {

        if (null == view) {
            throw new NullPointerException();
        }

        HasViewInjector hasViewInjector = findHasViewInjectors(view);

        Log.d(TAG, String.format("An injector for %s was found in %s", view.getClass().getCanonicalName(), hasViewInjector.getClass().getCanonicalName()));

        Map<Class<? extends View>, Provider<ViewComponentBuilder>> viewInjectors = hasViewInjector.viewInjectors();
        Provider<ViewComponentBuilder> provider = viewInjectors.get(view.getClass());

        if (provider != null) {
            ViewComponentBuilder viewComponentBuilder = provider.get();
            viewComponentBuilder
                    .bindInstance(view)
                    .build()
                    .injectMembers(view);
        } else {
            throw new NullPointerException("ViewInjection  fail ");
        }

    }

    private static HasViewInjector findHasViewInjectors(View view) {
        Context context = view.getContext();
        if (context instanceof HasViewInjector) {
            return (HasViewInjector) context;
        }
        Application application = (Application) context.getApplicationContext();
        if (application instanceof HasViewInjector) {
            return (HasViewInjector) application;
        }
        throw new IllegalArgumentException(String.format("No injector was found for %s", view.getClass().getCanonicalName()));
    }

}
