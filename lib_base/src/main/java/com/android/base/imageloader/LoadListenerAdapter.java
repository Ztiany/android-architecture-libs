package com.android.base.imageloader;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-09-22 13:14
 */
public abstract class LoadListenerAdapter<T> implements LoadListener<T> {

    @Override
    public void onLoadSuccess(T resource) {
    }

    @Override
    public void onLoadFail() {
    }

    @Override
    public void onLoadStart() {
    }
}
