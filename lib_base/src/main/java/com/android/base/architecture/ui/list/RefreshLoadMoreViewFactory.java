package com.android.base.architecture.ui.list;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * RefreshLoadMoreView Factory
 *
 * @author Ztiany
 * @version 1.0
 */
public class RefreshLoadMoreViewFactory {

    private static Factory sFactory;

    @NonNull
    public static RefreshLoadMoreView createRefreshLoadMoreView(View view) {
        if (sFactory != null) {
            return sFactory.createRefreshView(view);
        }
        throw new IllegalArgumentException("RefreshLoadViewFactory does not support create RefreshLoadMoreView . the view ：" + view);
    }

    public static void registerFactory(Factory factory) {
        sFactory = factory;
    }

    public interface Factory {
        RefreshLoadMoreView createRefreshView(View view);
    }

}
