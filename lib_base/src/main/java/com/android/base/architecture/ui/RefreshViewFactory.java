package com.android.base.architecture.ui;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * RefreshView Factory
 *
 * @author Ztiany
 *         Email: 1169654504@qq.com
 * @version 1.0
 */
public class RefreshViewFactory {

    private static Factory sFactory;

    public static RefreshView createRefreshView(View view) {
        if (sFactory != null) {
            return sFactory.createRefreshView(view);
        }
        if (view instanceof SwipeRefreshLayout) {
            return new SwipeRefreshView((SwipeRefreshLayout) view);
        }
        throw new IllegalArgumentException("RefreshViewFactory does not support create RefreshView . the view ：" + view);
    }

    public static void registerFactory(Factory factory) {
        sFactory = factory;
    }

    public interface Factory {
        RefreshView createRefreshView(View view);
    }
}
