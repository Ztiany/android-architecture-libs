package com.android.base.image;

public interface LoadListener<T> {

    default void onLoadStart() {
    }

    default void onLoadSuccess(T resource) {
    }

    default void onLoadFail() {
    }

}